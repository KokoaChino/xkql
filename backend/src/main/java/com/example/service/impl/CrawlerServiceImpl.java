package com.example.service.impl;

import com.example.service.api.CrawlerService;
import org.springframework.stereotype.Service;
import com.example.util.CrawlerUtil;
import java.util.List;
import java.util.Map;


@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Override
    public void getAllLinks(Map<String, String> params) {
        String username = params.get("username");
        String target = params.get("target");
        String mode = params.get("mode");
        String maxDeep = params.get("maxDeep");
        CrawlerUtil.getAllLinks(username, target, mode, maxDeep);
    }

    @Override
    public List<String> getAllLinks(String username) {
        return CrawlerUtil.getAllLinks(username);
    }

    @Override
    public void stopTask(String username) {
        CrawlerUtil.stopTask(username);
    }
}