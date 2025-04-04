package com.example.service.api;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Set;


public interface BatchImageWatermarkerService {
    void startTask(String username, byte[] fileBytes, Integer mode);
    Integer getProgress(String username);
    ResponseEntity<InputStreamResource> getZipFile(String username);
    Map<String, Set<String>> getAdditionalData(String username);
    void stopTask(String username);
}