package com.example.controller;

import com.example.service.api.CrawlerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {

    @Resource
    CrawlerService service;

    @PostMapping("/get-all-links")
    public void getAllLinks(@RequestBody Map<String, String> params) {
        service.getAllLinks(params);
    }

    @GetMapping("/get-all-links")
    public List<String> getAllLinks(@RequestParam("username") String username) {
        return service.getAllLinks(username);
    }

    @PostMapping("/stop-task")
    public void stopTask(@RequestParam("username") String username) {
        service.stopTask(username);
    }
}