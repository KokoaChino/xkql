package com.example.controller;

import com.example.service.api.BatchImageWatermarkerService;
import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api/batch-image-watermarker")
public class BatchImageWatermarkerController {

    @Resource
    BatchImageWatermarkerService service;


    @PostMapping("/start-task")
    public void startTask(@RequestParam("username") String username,
                          @RequestParam("file") MultipartFile file,
                          @RequestParam("mode") Integer mode) throws IOException {
        service.startTask(username, file.getBytes(), mode);
    }

    @GetMapping("/get-progress")
    Integer getProgress(@RequestParam("username") String username) {
        return service.getProgress(username);
    }

    @GetMapping("/get-zip-file")
    ResponseEntity<InputStreamResource> getZipFile(@RequestParam("username") String username) {
        return service.getZipFile(username);
    }

    @PostMapping("/stop-task")
    void stopTask(@RequestParam("username") String username) {
        service.stopTask(username);
    }

    @GetMapping("/get-additional-data")
    public Map<String, Set<String>> getAdditionalData(@RequestParam("username") String username) {
        return service.getAdditionalData(username);
    }
}