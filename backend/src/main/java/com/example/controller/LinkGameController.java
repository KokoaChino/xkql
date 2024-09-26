package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.entity.RestBean;
import com.example.mapper.LinkMapper;
import com.example.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("/link-game")
public class LinkGameController {

    @Resource
    LinkMapper linkMapper;

    @Resource
    UserMapper mapper;

    @PostMapping("/get-library")
    public int[][][][] getLibrary(@RequestBody int n) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = linkMapper.findLinkById(n);
        return objectMapper.readValue(json, int[][][][].class);
    }

    @PostMapping("/set-library")
    public RestBean<String> setLinkMapper(@RequestParam("json") String json, @RequestParam("n") int n) {
        linkMapper.updateLinkById(n, json);
        return RestBean.success("游戏库更新成功");
    }

    @PostMapping("/create-visited")
    public int[][] createVisited(@RequestBody int n) {
        return new int[n][n];
    }

    @PostMapping("/get-random")
    public int getRandom(@RequestBody int[] a) {
        if (a[0] <= 2) return 1;
        int res;
        do {
            res = new Random().nextInt(a[0] - 1) + 1;
        } while (res == a[1]);
        return res;
    }

    @PostMapping("/historical-record")
    public List<String> historicalRecord(@RequestBody String username) {
        username = username.substring(1, username.length() - 1);
        String data = mapper.findLinkGameHistoricalRecord(username);
        if (data == null) {
            List<String> res = new ArrayList<>(List.of("", "", "", "", "", "", "", "", "", ""));
            mapper.updateLinkGameHistoricalRecord(username, JSON.toJSONString(res));
            return res;
        } else {
            return JSON.parseObject(data, List.class);
        }
    }

    @PostMapping("/historical-record/update")
    public void updateHistoricalRecord(@RequestBody String[] parameters) {
        String username = parameters[0];
        int n = Integer.parseInt(parameters[1]);
        String time = parameters[2];
        String data = mapper.findLinkGameHistoricalRecord(username);
        List<String> a;
        if (data == null) {
            a = new ArrayList<>(List.of("", "", "", "", "", "", "", "", "", ""));
            mapper.updateLinkGameHistoricalRecord(username, JSON.toJSONString(a));
        } else a = JSON.parseObject(data, List.class);
        int i = n - 4;
        if (a.get(i).isEmpty()) a.set(i, time);
        else a.set(i, a.get(i).compareTo(time) < 0 ? a.get(i) : time);
        mapper.updateLinkGameHistoricalRecord(username, JSON.toJSONString(a));
    }

    @PostMapping("historical-record/reset")
    public void resetHistoricalRecord(@RequestBody String username) {
        username = username.substring(1, username.length() - 1);
        List<String> a = new ArrayList<>(List.of("", "", "", "", "", "", "", "", "", ""));
        mapper.updateLinkGameHistoricalRecord(username, JSON.toJSONString(a));
    }
}
