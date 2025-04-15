package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.converter.WatermarkParamsConverter;
import com.example.dto.WatermarkDataDTO;
import com.example.dto.WatermarkParamsDTO;
import com.example.entity.order.WatermarkData;
import com.example.entity.order.WatermarkParams;
import com.example.exception.FontNotFoundException;
import com.example.mapper.WatermarkMapper;
import com.example.service.api.BatchImageWatermarkerService;
import com.example.util.FileUtil;
import com.example.util.ImageUtil;
import jakarta.annotation.Resource;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class BatchImageWatermarkerServiceImpl implements BatchImageWatermarkerService {

    @Resource
    WatermarkMapper mapper;

    private static final Map<String, Thread> T = new ConcurrentHashMap<>();
    private static final Map<String, Integer> P = new ConcurrentHashMap<>();
    private static final Map<String, byte[]> ZIP = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Set<String>>> INFO = new ConcurrentHashMap<>();


    @Override
    public void startTask(String username, byte[] fileBytes, Integer mode) {
        Thread thread = new Thread(() -> {
            try {
                handleFileUpload(username, fileBytes, mode);
            } catch (InterruptedException e) {
                P.remove(username);
                T.remove(username);
                ZIP.remove(username);
                INFO.remove(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ZIP.remove(username);
        INFO.remove(username);
        T.put(username, thread);
        thread.start();
    }

    @Override
    public Integer getProgress(String username) {
        return P.getOrDefault(username, 0);
    }

    @Override
    public ResponseEntity<InputStreamResource> getZipFile(String username) {
        byte[] zipData = ZIP.getOrDefault(username, null);
        if (zipData == null) return null;
        return ResponseEntity.ok() // 构建响应，返回 ZIP 文件流
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=modified.zip") // 设置响应头，提示浏览器下载
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // 设置内容类型为二进制流
                .body(new InputStreamResource(new ByteArrayInputStream(zipData))); // 将字节数组转换为输入流
    }

    @Override
    public Map<String, Set<String>> getAdditionalData(String username) {
        return INFO.getOrDefault(username, null);
    }

    @Override
    public void stopTask(String username) {
        Thread thread = T.get(username);
        if (thread != null) thread.interrupt();
        P.remove(username);
        T.remove(username);
        ZIP.remove(username);
        INFO.remove(username);
    }

    @Override
    public List<WatermarkParamsDTO> getPresetStyleParams() {
        List<WatermarkParamsDTO> res = new ArrayList<>();
        for (WatermarkParams w : ImageUtil.P) {
            res.add(WatermarkParamsConverter.toDTO(w));
        }
        return res;
    }

    @Override
    public List<WatermarkDataDTO> getCustomStyleParams(String username) {
        List<WatermarkDataDTO> res = new ArrayList<>();
        List<WatermarkData> tmp = mapper.selectByUsername(username);
        for (WatermarkData d : tmp) {
            WatermarkDataDTO resDTO = new WatermarkDataDTO();
            resDTO.setId(d.getId());
            resDTO.setBackgroundImage(d.getBackgroundImage());
            resDTO.setModifiedImage(d.getModifiedImage());
            resDTO.setParams(JSON.parseObject(d.getJson(), new TypeReference<WatermarkParamsDTO>() {}));
            res.add(resDTO);
        }
        return res;
    }

    public void handleFileUpload(String username, byte[] fileBytes, Integer mode) throws IOException, InterruptedException {
        File tempDir = Files.createTempDirectory("temp").toFile();
        try {
            FileUtil.unzip(fileBytes, tempDir);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(bos)) { // 重新压缩修改后的文件到内存流
                AtomicReference<File> tableRef = new AtomicReference<>();
                File[] files = Objects.requireNonNull(tempDir.listFiles())[0].listFiles();
                String exMessage = check(files, tableRef);
                if (exMessage != null) {
                    throw new IllegalArgumentException(exMessage);
                }
                File table = tableRef.get();
                Map<String, String> map = new HashMap<>();
                DataFormatter dataFormatter = new DataFormatter();
                Set<String> successMatch = new HashSet<>(), tableNoMatch = map.keySet(), imageNoMatch = new HashSet<>();
                try (XSSFWorkbook workbook = new XSSFWorkbook(table)) {
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        String mid = dataFormatter.formatCellValue(sheet.getRow(i).getCell(0));
                        String price = dataFormatter.formatCellValue(sheet.getRow(i).getCell(1));
                        map.put(mid, price);
                    }
                    int validFilesCount = 0;
                    try {
                        String fontName = ImageUtil.P.get(mode - 1).getFontName();
                        if (!ImageUtil.check(fontName)) throw new FontNotFoundException("字体 [" + fontName + "] 未安装在系统中");
                    } catch (FontNotFoundException e) {
                        System.err.println("异常信息: " + e.getMessage());
                    }
                    for (File f: files) {
                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException("用户已停止任务");
                        }
                        String mid = f.getName();
                        String type = mid.substring(mid.lastIndexOf(".") + 1);
                        if (FileUtil.TABLE.contains(type)) continue;
                        mid = mid.substring(0, mid.lastIndexOf('.'));
                        String price = map.get(mid);
                        if (price == null) imageNoMatch.add(mid);
                        else {
                            ZipArchiveEntry entry = new ZipArchiveEntry(f.getName());
                            zipOut.putArchiveEntry(entry);
                            ImageUtil.handleImage(mode, f, price, zipOut);
                            zipOut.closeArchiveEntry();
                            tableNoMatch.remove(mid);
                            successMatch.add(mid);
                        }
                        int progress = ++validFilesCount * 100 / (files.length - 1);
                        if (progress == 100) progress = 99;
                        P.put(username, progress);
                    }
                } catch (InvalidFormatException e) {
                    throw new RuntimeException(e);
                } finally {
                    Map<String, Set<String>> info = new HashMap<>(
                            Map.ofEntries(
                                Map.entry("successMatch", successMatch),
                                Map.entry("tableNoMatch", tableNoMatch),
                                Map.entry("imageNoMatch", imageNoMatch)
                    ));
                    INFO.put(username, info);
                }
            }
            ZIP.put(username, bos.toByteArray());
            P.put(username, 100);
        } finally {
            FileUtil.deleteDirectory(tempDir);
        }
    }

    private String check(File[] files, AtomicReference<File> tableRef) {
        File table = null;
        for (File f: files) {
            String name = f.getName();
            String type = name.substring(name.lastIndexOf(".") + 1);
            if (FileUtil.TABLE.contains(type)) {
                if (table == null) table = f;
                else return "表格文件不唯一！";
            } else if (!FileUtil.IMAGE.contains(type)) {
                return "存在不支持的文件格式：" + type;
            }
        }
        if (table == null) return "表格文件不存在！";
        else tableRef.set(table);
        return null;
    }
}