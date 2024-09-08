package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.entity.impl.GuessNumberImpl;
import com.example.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/guess-number")
public class GuessNumberController {

    @Resource
    UserMapper mapper;

    @PostMapping("/game/start")
    public void StartGuessNumber(@RequestBody int max, HttpSession session) {
        GuessNumberImpl g = new GuessNumberImpl(max);
        session.setAttribute("guessNumber", g);
    }

    @PostMapping("/play")
    public GuessNumberImpl playGuessNumber(@RequestBody int cur, HttpSession session) {
        GuessNumberImpl g = (GuessNumberImpl) session.getAttribute("guessNumber");
        g.setCnt(g.getCnt() + 1);
        if (g.getTarget() == cur) {
            g.getList().add(0, String.format("【%d】你猜的数字为：%d，猜对了！", g.getCnt(), cur));
        } else if (g.getTarget() > cur) {
            g.getList().add(0, String.format("【%d】你猜的数字为：%d，猜小了！", g.getCnt(), cur));
        } else {
            g.getList().add(0, String.format("【%d】你猜的数字为：%d，猜大了！", g.getCnt(), cur));
        }
        session.setAttribute("guessNumber", g);
        return g;
    }

    @GetMapping("/init")
    public String initGuessNumber(HttpSession session) {
        GuessNumberImpl g = (GuessNumberImpl) session.getAttribute("guessNumber");
        return String.valueOf(g.getMax());
    }

    @PostMapping("/historical-record")
    public List<Integer> historicalRecord(@RequestBody String username) {
        username = username.substring(1, username.length() - 1);
        String data = mapper.findGuessNumberHistoricalRecord(username);
        if (data == null) {
            List<Integer> res = new ArrayList<>(List.of(0, 0, 0, 0, 0, 0, 0, 0));
            mapper.updateGuessNumberHistoricalRecord(username, JSON.toJSONString(res));
            return res;
        } else {
            return JSON.parseObject(data, List.class);
        }
    }

    @PostMapping("/historical-record/update")
    public void updateHistoricalRecord(@RequestBody String username, HttpSession session) {
        username = username.substring(1, username.length() - 1);
        GuessNumberImpl g = (GuessNumberImpl) session.getAttribute("guessNumber");
        int max = g.getMax(), cnt = g.getCnt();
        String data = mapper.findGuessNumberHistoricalRecord(username);
        List<Integer> a;
        if (data == null) {
            a = new ArrayList<>(List.of(0, 0, 0, 0, 0, 0, 0, 0));
            mapper.updateGuessNumberHistoricalRecord(username, JSON.toJSONString(a));
        } else a = JSON.parseObject(data, List.class);
        int i = String.valueOf(max).length() - 2;
        if (a.get(i) == 0) a.set(i, cnt);
        else a.set(i, Math.min(a.get(i), cnt));
        mapper.updateGuessNumberHistoricalRecord(username, JSON.toJSONString(a));
    }

    @PostMapping("historical-record/reset")
    public void resetHistoricalRecord(@RequestBody String username) {
        username = username.substring(1, username.length() - 1);
        ArrayList<Integer> a = new ArrayList<>(List.of(0, 0, 0, 0, 0, 0, 0, 0));
        mapper.updateGuessNumberHistoricalRecord(username, JSON.toJSONString(a));
    }
}