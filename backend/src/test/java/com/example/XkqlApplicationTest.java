package com.example;

import com.example.service.api.CrawlerService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;


@SpringBootTest
public class XkqlApplicationTest {

    @Resource
    CrawlerService service;

    String username = "星开祈灵";

    @Test
    void test() {
        List<Integer> a = new ArrayList<>();
        System.out.println(a);
        fun(a);
        System.out.println(a);
    }

    void fun(List<Integer> a) {
        a.add(1);
    }
}