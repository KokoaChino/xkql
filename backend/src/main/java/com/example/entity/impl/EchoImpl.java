package com.example.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
@AllArgsConstructor
public class EchoImpl {

    public String main;
    public String cost;
    public List<List<String>> echo;
    public String score;

    public EchoImpl() {}
    public static EchoImpl getEmpty() {
        List<List<String>> echo = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<String> tmp = new ArrayList<>(Arrays.asList("", "", ""));
            echo.add(tmp);
        }
        return new EchoImpl("", "", echo, "");
    }
}