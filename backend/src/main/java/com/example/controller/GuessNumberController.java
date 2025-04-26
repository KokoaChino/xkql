package com.example.controller;

import com.example.entity.other.GuessNumber;
import com.example.service.api.GuessNumberService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/guess-number")
public class GuessNumberController {

    @Resource
    GuessNumberService service;

    @PostMapping("/game/start")
    public void StartGuessNumber(@RequestBody int max, HttpSession session) {
        GuessNumber g = new GuessNumber(max);
        session.setAttribute("guessNumber", g);
    }

    @PostMapping("/play")
    public GuessNumber playGuessNumber(@RequestBody int cur, HttpSession session) {
        GuessNumber g = (GuessNumber) session.getAttribute("guessNumber");
        session.setAttribute("guessNumber", service.playGuessNumber(cur, g));
        return g;
    }

    @GetMapping("/init")
    public String initGuessNumber(HttpSession session) {
        GuessNumber g = (GuessNumber) session.getAttribute("guessNumber");
        return String.valueOf(g.getMax());
    }

    @PostMapping("/historical-record")
    public List<Integer> historicalRecord(@RequestBody String username) {
        username = username.substring(1, username.length() - 1);
        return service.historicalRecord(username);
    }

    @PostMapping("/historical-record/update")
    public void updateHistoricalRecord(@RequestBody String username, HttpSession session) {
        username = username.substring(1, username.length() - 1);
        GuessNumber g = (GuessNumber) session.getAttribute("guessNumber");
        service.updateHistoricalRecord(username, g);
    }

    @PostMapping("/historical-record/reset")
    public void resetHistoricalRecord(@RequestBody String username) {
        username = username.substring(1, username.length() - 1);
        service.resetHistoricalRecord(username);
    }
}