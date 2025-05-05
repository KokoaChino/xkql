package com.example.service.impl;

import com.example.convert.WatermarkParamsConvert;
import com.example.dto.WatermarkDataDTO;
import com.example.dto.WatermarkParamsDTO;
import com.example.entity.other.ImageInfo;
import com.example.entity.other.WatermarkData;
import com.example.entity.other.WatermarkParams;
import com.example.exception.FontNotFoundException;
import com.example.mapper.WatermarkMapper;
import com.example.service.api.BatchImageWatermarkerService;
import com.example.util.FileUtil;
import com.example.util.ImageUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Slf4j
@Service
public class BatchImageWatermarkerServiceImpl implements BatchImageWatermarkerService {

    @Resource
    WatermarkMapper mapper;

    private static final Map<String, Future<?>> T = new ConcurrentHashMap<>();
    private static final Map<String, Integer> P = new ConcurrentHashMap<>();
    private static final Map<String, File> ZIP = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Set<String>>> INFO = new ConcurrentHashMap<>();

    private final ExecutorService executor = new ThreadPoolExecutor(
            2,
            6,
            30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Override
    public void startTask(String username, InputStream fileStream, WatermarkParams params, Image backgroundImage) {
        cleanupResources(username);
        Future<?> future = executor.submit(() -> {
            try {
                handleFileUpload(username, fileStream, params, backgroundImage);
            } catch (Exception e) {
                cleanupResources(username);
                log.error("进程崩溃：{}", e.getMessage());
                throw new RuntimeException(e);
            }
        });
        T.put(username, future);
    }

    private void cleanupResources(String username) {
        P.remove(username);
        T.remove(username);
        INFO.remove(username);
        ZIP.remove(username);
    }

    @Override
    public Integer getProgress(String username) {
        return P.getOrDefault(username, 0);
    }

    @Override
    public ResponseEntity<InputStreamResource> getZipFile(String username) {
        File file = ZIP.getOrDefault(username, null);
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=modified.zip")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(new FileInputStream(file)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new InputStreamResource(new ByteArrayInputStream("{\"message\":\"服务器内部错误\"}".getBytes())));
        }
    }

    @Override
    public Map<String, Set<String>> getAdditionalData(String username) {
        return INFO.getOrDefault(username, null);
    }

    @Override
    public void stopTask(String username) {
        Future<?> future = T.get(username);
        if (future != null) future.cancel(true);
        cleanupResources(username);
    }

    @Override
    public List<WatermarkParamsDTO> getPresetStyleParams() {
        List<WatermarkParamsDTO> res = new ArrayList<>();
        for (WatermarkParams w : ImageUtil.P) {
            res.add(WatermarkParamsConvert.toDTO(w));
        }
        return res;
    }

    @Override
    public List<WatermarkDataDTO> getCustomStyleParams(String username) {
        List<WatermarkDataDTO> res = new ArrayList<>();
        List<WatermarkData> tmp = mapper.selectByUsername(username);
        for (WatermarkData w : tmp) {
            res.add(WatermarkParamsConvert.toDTO(w));
        }
        res.sort(Comparator.comparing(WatermarkDataDTO::getId));
        return res;
    }

    @Override
    public void addCustomStyleParams(WatermarkDataDTO data) {
        mapper.insert(WatermarkParamsConvert.toEntity(data));
    }

    @Override
    public void deleteCustomStyleParams(Integer id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateCustomStyleParams(WatermarkDataDTO data) {
        mapper.update(WatermarkParamsConvert.toEntity(data));
    }

    @Override
    public String getDefaultBackgroundImage() {
        File imageFile = new File("backend/static/图片/默认背景图.jpg");
        try (InputStream inputStream = new FileInputStream(imageFile)) {
            byte[] bytes = inputStream.readAllBytes();
            return WatermarkParamsConvert.toBase64(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getModifiedImage(WatermarkParams params, byte[] bytes) throws FontNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            BufferedImage image = ImageIO.read(bis);
            if (image == null) {
                throw new IllegalArgumentException("无法解析图像数据（格式不支持或数据损坏）");
            }
            BufferedImage bufferedImage = ImageUtil.handleWatermark(params, image, null, "1234");
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(bufferedImage, "jpeg", baos);
                baos.flush();
                return WatermarkParamsConvert.toBase64(baos.toByteArray());
            }
        } catch (IOException e) {
            throw new RuntimeException("读取图像数据时发生错误", e);
        }
    }

    @Override
    public String handleUploadFontFile(byte[] fileBytes, String fileName) {
        if (!fileName.contains(".")) throw new IllegalArgumentException("文件名格式错误");
        String type = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!ImageUtil.FONT_TYPE.contains(type.toLowerCase())) throw new IllegalArgumentException("不支持写入." + type + "格式的字体文件");
        String name = "[" + fileName.substring(0, fileName.lastIndexOf(".")) + "]-" + UUID.randomUUID().toString().substring(0, 8);
        fileName = name + "." + type;
        File file = new File(ImageUtil.UPLOAD_FONT_PATH, fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException("写入文件失败: " + fileName, e);
        }
        ImageUtil.UPLOAD_FONT_MAP.put(name, type);
        return fileName;
    }

    public void handleFileUpload(String username, InputStream fileStream, WatermarkParams params, Image backgroundImage) throws IOException, InterruptedException, FontNotFoundException {
        File input = Files.createTempDirectory("input").toFile();
        File output = Files.createTempFile("output", ".zip").toFile();
        try {
            FileUtil.unzip(fileStream, input);
            try (FileOutputStream fos = new FileOutputStream(output);
                 ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(fos)) {
                AtomicReference<File> tableRef = new AtomicReference<>();
                File[] files = Objects.requireNonNull(input.listFiles())[0].listFiles();
                String exMessage = check(files, tableRef);
                if (exMessage != null) {
                    throw new IllegalArgumentException(exMessage);
                }
                File table = tableRef.get();
                Map<String, ImageInfo> map = new HashMap<>();
                Map<String, Integer> entriesCnt = new HashMap<>();
                DataFormatter dataFormatter = new DataFormatter();
                Set<String> successMatch = new HashSet<>(), tableNoMatch = map.keySet(), imageNoMatch = new HashSet<>();
                try (XSSFWorkbook workbook = new XSSFWorkbook(table)) {
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        String mid = dataFormatter.formatCellValue(row.getCell(0));
                        String price = dataFormatter.formatCellValue(row.getCell(1));
                        String fileName = dataFormatter.formatCellValue(row.getCell(2));
                        fileName = fileName.isEmpty() ? mid : fileName;
                        boolean flag = FileUtil.checkFileName(fileName);
                        fileName += ".jpg";
                        List<String> pathSegments = new ArrayList<>();
                        int colIdx = 3;
                        while (true) {
                            String segment = dataFormatter.formatCellValue(row.getCell(colIdx++));
                            if (segment == null || segment.trim().isEmpty()) break;
                            if (FileUtil.checkFileName(segment)) {
                                flag = true;
                                break;
                            }
                            pathSegments.add(segment);
                        }
                        String fullPath = pathSegments.isEmpty()
                                ? fileName
                                : String.join("/", pathSegments) + "/" + fileName;
                        if (flag) {
                            fullPath = FileUtil.ERROR_PATH + "/" + mid + ".jpg";
                        }
                        int cnt = entriesCnt.getOrDefault(fullPath, 0);
                        if (cnt > 0) {
                            int idx = fullPath.lastIndexOf('.');
                            String prefix = fullPath.substring(0, idx), suffix = fullPath.substring(idx);
                            fullPath = prefix + "(" + cnt + ")" + suffix;
                        }
                        ImageInfo imageInfo = new ImageInfo(mid, price, fullPath);
                        map.put(mid, imageInfo);
                        entriesCnt.merge(fullPath, 1, Integer::sum);
                    }
                    int validFilesCount = 0;
                    try {
                        String fontName = params.getFontName();
                        if (ImageUtil.getFont(fontName, 0, 0) == null) throw new FontNotFoundException("字体 [" + fontName + "] 未安装在系统中");
                    } catch (FontNotFoundException e) {
                        log.error("异常信息: {}", e.getMessage());
                    }
                    for (File f: files) {
                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException("用户已停止任务");
                        }
                        String mid = f.getName();
                        String type = mid.substring(mid.lastIndexOf(".") + 1);
                        if (FileUtil.TABLE.contains(type)) continue;
                        mid = mid.substring(0, mid.lastIndexOf('.'));
                        ImageInfo imageInfo = map.get(mid);
                        if (imageInfo == null) imageNoMatch.add(mid);
                        else {
                            String price = imageInfo.getPrice(), path = imageInfo.getPath();
                            ZipArchiveEntry entry = new ZipArchiveEntry(path);
                            zipOut.putArchiveEntry(entry);
                            ImageUtil.handleImage(params, ImageIO.read(f), price, backgroundImage, zipOut);
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
                                Map.entry("successMatch", filterEmptyStrings(successMatch)),
                                Map.entry("tableNoMatch", filterEmptyStrings(tableNoMatch)),
                                Map.entry("imageNoMatch", filterEmptyStrings(imageNoMatch))
                    ));
                    INFO.put(username, info);
                }
            }
            ZIP.put(username, output);
            P.put(username, 100);
        } finally {
            FileUtil.deleteDirectory(input);
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

    private Set<String> filterEmptyStrings(Collection<String> collection) {
        return collection.stream()
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Scheduled(fixedRate = 3600 * 1000)
    public void cleanupTempFiles() {
        String tmpDirPath = System.getProperty("java.io.tmpdir");
        File tempDir = new File(tmpDirPath);
        File[] files = tempDir.listFiles((dir, name) ->
                name.toLowerCase().startsWith("output") && name.toLowerCase().endsWith(".zip")
        );
        if (files != null) {
            long now = System.currentTimeMillis();
            for (File file : files) {
                try {
                    if (now - file.lastModified() > 3600 * 1000) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            log.info("已清理过期临时文件: {}", file.getAbsolutePath());
                        } else {
                            log.error("文件清理失败: {}", file.getAbsolutePath());
                        }
                    }
                } catch (SecurityException e) {
                    log.error("权限不足，无法删除文件: {}", file.getAbsolutePath());
                }
            }
        }
    }
}