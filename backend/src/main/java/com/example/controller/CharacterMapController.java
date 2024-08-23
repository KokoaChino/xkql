package com.example.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/character-map")
public class CharacterMapController {

    private static final Map<String, Integer> SHOW = new HashMap<>();

    static {
        SHOW.put("保登心爱", 34);
        SHOW.put("豆馅", 1);
        SHOW.put("奈津惠", 15);
        SHOW.put("青山蓝山", 12);
        SHOW.put("提比", 4);
        SHOW.put("天天座理世", 41);
        SHOW.put("条河麻耶", 10);
        SHOW.put("桐间纱路", 50);
        SHOW.put("香风智乃", 61);
        SHOW.put("宇治松千夜", 31);
        SHOW.put("塞西莉亚", 41);
        SHOW.put("宇佐美瑞希", 1);
    }

    @GetMapping("/getCharacterMapCatalogue")
    public List<List<String>> getCharacterMapCatalogue() {
        Path rootPath = Paths.get("").toAbsolutePath().getParent();
        File characterMap = new File(rootPath.toString() + "/xkql/frontend/public/角色图");
        File[] files = characterMap.listFiles();
        List<List<String>> res = new ArrayList<>();
        for (File file : files) {
            res.add(new ArrayList<>(List.of(file.getName(), Objects.requireNonNull(file.listFiles())[SHOW.get(file.getName()) - 1].getName())));
        }
        return res;
    }

    @PostMapping("setName")
    public void setName(@RequestBody String name, HttpSession session) {
        session.setAttribute("characterName", name);
    }

    @PostMapping("getName")
    public String getName(HttpSession session) {
        return (String) session.getAttribute("characterName");
    }

    @PostMapping("getCharacterMapFiles")
    public List<List<String>> getCharacterMapFiles(@RequestBody String name) {
        name = name.substring(1, name.length() - 1);
        Path rootPath = Paths.get("").toAbsolutePath().getParent();
        File characterMap = new File(rootPath.toString() + "/xkql/frontend/public/角色图/" + name);
        File[] files = characterMap.listFiles();
        List<List<String>> res = new ArrayList<>();
        for (File file : files) {
            Date lastModifiedDate = new Date(file.lastModified());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(lastModifiedDate);
            String fileName = file.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            res.add(new ArrayList<>(List.of(fileName, formattedDate, file.getName())));
        }
        return res;
    }
}