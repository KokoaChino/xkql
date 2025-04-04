package com.example.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.*;
import java.nio.file.Files;
import java.util.*;


public class FileUtil {

    public static final Set<String> TABLE = new HashSet<>(List.of( // 支持的表格形式
            "xls", "xlsx"
    ));
    public static final Set<String> IMAGE = new HashSet<>(List.of( // 支持的图片形式
            "jpg", "jpeg", "png", "webp"
    ));

    public static void unzip(byte[] fileBytes, File destDir) throws IOException { // 解压 ZIP 文件到目标目录
        try (ZipArchiveInputStream zipIn = new ZipArchiveInputStream(new ByteArrayInputStream(fileBytes))) {
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
}