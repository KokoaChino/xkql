package com.example;

import com.example.service.api.CrawlerService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
public class XkqlApplicationTest {

    @Resource
    CrawlerService service;

    String username = "星开祈灵";

    @Test
    void test() throws InterruptedException {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("target", "https://leetcode.cn/problemset/");
        params.put("mode", "all");
        params.put("maxDeep", "10");
        service.getAllLinks(params);
        while (true) {
            Thread.sleep(1000);
            List<String> allLinks = service.getAllLinks(username);
            System.out.println(allLinks);
            if (!allLinks.isEmpty() && allLinks.get(allLinks.size() - 1) == null) break;
        }
    }
}