package com.example.controller;

import com.example.entity.common.RestBean;
import com.example.service.api.LinkGameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/link-game")
public class LinkGameController {

    @Resource
    LinkGameService service;

    @PostMapping("/get-library")
    public int[][][][] getLibrary(@RequestBody int n) throws JsonProcessingException {
        return service.getLibrary(n);
    }

    @PostMapping("/set-library")
    public RestBean<String> setLinkMapper(@RequestParam("json") String json, @RequestParam("n") int n) {
        return service.setLinkMapper(json, n);
    }

    @PostMapping("/create-visited")
    public int[][] createVisited(@RequestBody int n) {
        return new int[n][n];
    }

    @PostMapping("/get-random")
    public int getRandom(@RequestBody int[] a) {
        return service.getRandom(a);
    }

    @PostMapping("/historical-record")
    public List<String> historicalRecord(@RequestBody String username) {
        username = username.substring(1, username.length() - 1);
        return service.historicalRecord(username);
    }

    @PostMapping("/historical-record/update")
    public void updateHistoricalRecord(@RequestBody String[] parameters) {
        service.updateHistoricalRecord(parameters);
    }

    @PostMapping("/historical-record/reset")
    public void resetHistoricalRecord(@RequestBody String username) {
        username = username.substring(1, username.length() - 1);
        service.resetHistoricalRecord(username);
    }
}