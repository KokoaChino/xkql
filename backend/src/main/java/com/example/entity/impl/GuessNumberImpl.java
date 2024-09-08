package com.example.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Data
@AllArgsConstructor
public class GuessNumberImpl {
    int cnt = 0, target, max;
    List<String> list = new ArrayList<>();

    public GuessNumberImpl(int max) {
        this.max = max;
        target = new Random().nextInt(max + 1);
    }
    public GuessNumberImpl() {}
}