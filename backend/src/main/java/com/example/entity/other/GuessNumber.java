package com.example.entity.other;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Data
@AllArgsConstructor
public class GuessNumber {
    int cnt = 0, target, max;
    List<String> list = new ArrayList<>();

    public GuessNumber(int max) {
        this.max = max;
        target = new Random().nextInt(max + 1);
    }
    public GuessNumber() {}
}