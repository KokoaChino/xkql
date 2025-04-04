package com.example.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CrawlerUtil {

    private static final Pattern mainHostPattern = Pattern.compile("^(https?://[^/\\s]+)");
    private static final Pattern pattern = Pattern.compile(
            "https?://[^\\s\"'<>]+",
            Pattern.CASE_INSENSITIVE
    );
    public static final Set<String> DOWNLOADABLE_SUFFIXES = new HashSet<>(Arrays.asList(
            // 视频格式
            "mp4", "avi", "mov", "mkv", "flv", "wmv", "webm", "m4v", "3gp", "mpg", "mpeg",
            // 音频格式
            "mp3", "wav", "flac", "aac", "wma", "ogg", "m4a", "midi",
            // 文档格式
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "rtf", "md",
            "csv", "json", "xml", "html", "htm", "tex", "epub", "mobi", "azw",
            // 压缩文件
            "zip", "rar", "7z", "tar", "gz", "bz2", "xz", "iso", "dmg",
            // 图片格式
            "jpg", "jpeg", "png", "gif", "bmp", "svg", "webp", "psd", "ai", "eps", "tif", "tiff",
            // 可执行文件
            "exe", "msi", "apk", "deb", "rpm", "appimage", "bat", "sh", "jar",
            // 数据文件
            "db", "sql", "dbf", "dat", "log", "bak", "tmp",
            // 其他常见下载类型
            "torrent", "sub", "srt", "ass", "vtt", "ics", "cert", "pem", "ppk"
    ));

    private static final Map<String, Map<String, String>> P = new ConcurrentHashMap<>();
    private static final Map<String, Thread> T = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> URLS = new ConcurrentHashMap<>();

    public static boolean isDownloadableFile(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        String cleanUrl = removeQueryAndFragment(url);
        String filename = extractFilename(cleanUrl);
        if (filename.isEmpty()) {
            return false;
        }
        String extension = getFileExtension(filename);
        return !extension.isEmpty() &&
                DOWNLOADABLE_SUFFIXES.contains(extension.toLowerCase());
    }
    private static String removeQueryAndFragment(String url) {
        int queryIndex = url.indexOf('?');
        int fragmentIndex = url.indexOf('#');
        int endIndex = Math.min(
                queryIndex == -1 ? url.length() : queryIndex,
                fragmentIndex == -1 ? url.length() : fragmentIndex
        );
        return url.substring(0, endIndex);
    }
    private static String extractFilename(String url) {
        String normalizedUrl = url.replaceAll("/+", "/");
        int lastSlashIndex = normalizedUrl.lastIndexOf('/');
        return (lastSlashIndex >= 0 && lastSlashIndex < normalizedUrl.length() - 1)
                ? normalizedUrl.substring(lastSlashIndex + 1)
                : "";
    }
    private static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return (lastDotIndex != -1 && lastDotIndex < filename.length() - 1)
                ? filename.substring(lastDotIndex + 1)
                : "";
    }

    private static String getNewLink(Matcher matcher, String mainHost) {
        String newLink = matcher.group().trim();
        if (newLink.startsWith("//")) {
            newLink = mainHost.split("//")[0] + "//" + newLink.substring(2);
        } else if (!newLink.startsWith("http")) {
            if (newLink.startsWith("/")) {
                newLink = mainHost + newLink;
            } else {
                newLink = mainHost + "/" + newLink;
            }
        }
        if (newLink.endsWith("/")) {
            newLink = newLink.substring(0, newLink.length() - 1);
        }
        return newLink;
    }

    public static void getAllLinks(String username, String baseUrl, String mode, String maxDeep) {
        Map<String, String> params = new HashMap<>();
        params.put("mode", mode);
        params.put("maxDeep", maxDeep);
        Matcher matcher = mainHostPattern.matcher(baseUrl);
        if (matcher.find()) {
            params.put("mainHost", matcher.group(1));
        }
        P.put(username, params);
        URLS.put(username, new HashSet<>());
        Thread thread = new Thread(() -> {
            try {
                dfs(username, 0, baseUrl);
                if (mode.equals("download")) {
                    if (isDownloadableFile(baseUrl)) URLS.get(username).add(baseUrl);
                } else URLS.get(username).add(baseUrl);
            } catch (InterruptedException e) {
                P.remove(username);
                T.remove(username);
                URLS.remove(username);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        T.put(username, thread);
        thread.start();
    }

    private static void dfs(String username, int deep, String link) throws Exception {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("用户已停止任务");
        }
        Set<String> links = URLS.get(username);
        String mode = P.get(username).get("mode");
        int maxDeep = Integer.parseInt(P.get(username).get("maxDeep"));
        if (deep == maxDeep || links.contains(link)) return;
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(link).openConnection();
        httpURLConnection.setRequestMethod("GET");
        if (httpURLConnection.getResponseCode() == 200) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        String mainHost = P.get(username).get("mainHost");
                        String newLink = getNewLink(matcher, mainHost);
                        if (mode.equals("download")) {
                            if (isDownloadableFile(newLink)) URLS.get(username).add(newLink);
                        } else URLS.get(username).add(newLink);
                        try {
                            URI mainUri = new URI(mainHost), newUri = new URI(newLink);
                            if (mainUri.getHost().equals(newUri.getHost())) {
                                dfs(username, deep + 1, newLink);
                            }
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } finally {
                httpURLConnection.disconnect();
            }
        }
    }

    public static List<String> getAllLinks(String username) {
        List<String> links = new ArrayList<>(URLS.get(username).stream().sorted().toList());
        if (!T.get(username).isAlive()) links.add(null);
        return links;
    }

    public static void stopTask(String username) {
        Thread thread = T.get(username);
        if (thread != null) thread.interrupt();
        P.remove(username);
        T.remove(username);
        URLS.remove(username);
    }
}