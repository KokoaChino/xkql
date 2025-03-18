package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.entity.common.RestBean;
import com.example.mapper.LinkMapper;
import com.example.mapper.UserMapper;
import com.example.service.api.LinkGameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class LinkGameServiceImpl implements LinkGameService {

    @Resource
    LinkMapper linkMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public int[][][][] getLibrary(int n) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = linkMapper.findLinkById(n);
        return objectMapper.readValue(json, int[][][][].class);
    }

    @Override
    public RestBean<String> setLinkMapper(String json, int n) {
        linkMapper.updateLinkById(n, json);
        return RestBean.success("游戏库更新成功");
    }

    @Override
    public int getRandom(int[] a) {
        if (a[0] <= 2) return 1;
        int res;
        do {
            res = new Random().nextInt(a[0] - 1) + 1;
        } while (res == a[1]);
        return res;
    }

    @Override
    public List<String> historicalRecord(String username) {
        String data = userMapper.findLinkGameHistoricalRecord(username);
        if (data == null) {
            List<String> res = new ArrayList<>(List.of("", "", "", "", "", "", "", "", "", ""));
            userMapper.updateLinkGameHistoricalRecord(username, JSON.toJSONString(res));
            return res;
        } else {
            return JSON.parseObject(data, new TypeReference<List<String>>() {});
        }
    }

    @Override
    public void updateHistoricalRecord(String[] parameters) {
        String username = parameters[0];
        int n = Integer.parseInt(parameters[1]);
        String time = parameters[2];
        String data = userMapper.findLinkGameHistoricalRecord(username);
        List<String> a;
        if (data == null) {
            a = new ArrayList<>(List.of("", "", "", "", "", "", "", "", "", ""));
            userMapper.updateLinkGameHistoricalRecord(username, JSON.toJSONString(a));
        } else a = JSON.parseObject(data, new TypeReference<List<String>>() {});
        int i = n - 4;
        if (a.get(i).isEmpty()) a.set(i, time);
        else a.set(i, a.get(i).compareTo(time) < 0 ? a.get(i) : time);
        userMapper.updateLinkGameHistoricalRecord(username, JSON.toJSONString(a));
    }

    @Override
    public void resetHistoricalRecord(String username) {
        List<String> a = new ArrayList<>(List.of("", "", "", "", "", "", "", "", "", ""));
        userMapper.updateLinkGameHistoricalRecord(username, JSON.toJSONString(a));
    }
}