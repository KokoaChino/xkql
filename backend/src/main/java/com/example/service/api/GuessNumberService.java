package com.example.service.api;

import com.example.entity.other.GuessNumber;
import java.util.List;


public interface GuessNumberService {
    GuessNumber playGuessNumber(int cur, GuessNumber g);
    List<Integer> historicalRecord(String username);
    void updateHistoricalRecord(String username, GuessNumber g);
    void resetHistoricalRecord(String username);
}