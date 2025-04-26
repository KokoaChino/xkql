package com.example.service.api;

import com.example.entity.common.RestBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;


public interface LinkGameService {
    int[][][][] getLibrary(int n) throws JsonProcessingException;
    RestBean<String> setLinkMapper(String json, int n);
    int getRandom(int[] a);
    List<String> historicalRecord(String username);
    void updateHistoricalRecord(String[] parameters);
    void resetHistoricalRecord(String username);
}