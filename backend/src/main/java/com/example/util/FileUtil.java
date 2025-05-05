package com.example.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;


public class FileUtil {

    public static final Set<String> TABLE = new HashSet<>(List.of( // 支持的表格形式
            "xls", "xlsx"
    ));
    public static final Set<String> IMAGE = new HashSet<>(List.of( // 支持的图片形式
            "jpg", "jpeg", "png", "webp"
    ));
    public static final Pattern ERROR_PATTERN = Pattern.compile("[\\\\/:*?\"<>|]");
    public static final String ERROR_PATH = "ERROR-" + UUID.randomUUID().toString().substring(0, 8);

    public static void unzip(InputStream fileStream, File destDir) throws IOException { // 解压 ZIP 文件到目标目录
        try (ZipArchiveInputStream zipIn = new ZipArchiveInputStream(fileStream)) {
            ZipArchiveEntry entry;
            while ((entry = zipIn.getNextZipEntry()) != null) {
                File entryDestination = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    Files.createDirectories(entryDestination.getParentFile().toPath());
                    try (FileOutputStream fos = new FileOutputStream(entryDestination)) {
                        IOUtils.copy(zipIn, fos);
                    }
                }
            }
        }
    }

    public static void deleteDirectory(File dir) { // 递归删除目录
        if (dir.exists()) {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.isDirectory()) deleteDirectory(file);
                file.delete();
            }
            dir.delete();
        }
    }

    public static boolean checkFileName(String name) { // 判断文件名合法性
        return ERROR_PATTERN.matcher(name).find();
    }
}