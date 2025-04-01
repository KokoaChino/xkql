package com.example.service.api;

import java.util.List;
import java.util.Map;


public interface CrawlerService {
    void getAllLinks(Map<String, String> params);
    List<String> getAllLinks(String username);
    void stopTask(String username);
}