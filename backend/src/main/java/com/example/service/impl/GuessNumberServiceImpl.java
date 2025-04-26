package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.entity.other.GuessNumber;
import com.example.mapper.UserMapper;
import com.example.service.api.GuessNumberService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class GuessNumberServiceImpl implements GuessNumberService {

    @Resource
    UserMapper mapper;

    @Override
    public GuessNumber playGuessNumber(int cur, GuessNumber g) {
        g.setCnt(g.getCnt() + 1);
        if (g.getTarget() == cur) {
            g.getList().add(0, String.format("【%d】你猜的数字为：%d，猜对了！", g.getCnt(), cur));
        } else if (g.getTarget() > cur) {
            g.getList().add(0, String.format("【%d】你猜的数字为：%d，猜小了！", g.getCnt(), cur));
        } else {
            g.getList().add(0, String.format("【%d】你猜的数字为：%d，猜大了！", g.getCnt(), cur));
        }
        return g;
    }

    @Override
    public List<Integer> historicalRecord(String username) {
        String data = mapper.findGuessNumberHistoricalRecord(username);
        if (data == null) {
            List<Integer> res = new ArrayList<>(List.of(0, 0, 0, 0, 0, 0, 0, 0));
            mapper.updateGuessNumberHistoricalRecord(username, JSON.toJSONString(res));
            return res;
        } else {
            return JSON.parseObject(data, new TypeReference<List<Integer>>() {});
        }
    }

    @Override
    public void updateHistoricalRecord(String username, GuessNumber g) {
        int max = g.getMax(), cnt = g.getCnt();
        String data = mapper.findGuessNumberHistoricalRecord(username);
        List<Integer> a;
        if (data == null) {
            a = new ArrayList<>(List.of(0, 0, 0, 0, 0, 0, 0, 0));
            mapper.updateGuessNumberHistoricalRecord(username, JSON.toJSONString(a));
        } else a = JSON.parseObject(data, new TypeReference<List<Integer>>() {});
        int i = String.valueOf(max).length() - 2;
        if (a.get(i) == 0) a.set(i, cnt);
        else a.set(i, Math.min(a.get(i), cnt));
        mapper.updateGuessNumberHistoricalRecord(username, JSON.toJSONString(a));
    }

    @Override
    public void resetHistoricalRecord(String username) {
        ArrayList<Integer> a = new ArrayList<>(List.of(0, 0, 0, 0, 0, 0, 0, 0));
        mapper.updateGuessNumberHistoricalRecord(username, JSON.toJSONString(a));
    }
}