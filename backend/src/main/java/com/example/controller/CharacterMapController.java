package com.example.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@RestController
@RequestMapping("/character-map")
public class CharacterMapController {

    private static final Map<String, Integer> SHOW = new HashMap<>();

    static {
        SHOW.put("保登摩卡", 3);
        SHOW.put("保登心爱", 565);
        SHOW.put("豆馅", 4);
        SHOW.put("奈津惠", 72);
        SHOW.put("青山蓝山", 23);
        SHOW.put("塞西莉亚", 41);
        SHOW.put("提比", 34);
        SHOW.put("天天座理世", 48);
        SHOW.put("条河麻耶", 10);
        SHOW.put("桐间纱路", 154);
        SHOW.put("香风智乃", 101);
        SHOW.put("野雁", 6);
        SHOW.put("宇治松千夜", 188);
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
            List<String> last = res.get(res.size() - 1);
            last.add("/角色图压缩/" + last.get(0) + ".zip");
            last.add(last.get(0) + ".zip");
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
            double size = file.length() / 1024.0;
            String fileName = file.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            res.add(new ArrayList<>(List.of(fileName, String.format("%.2f KB", size), file.getName())));
        }
        return res;
    }
}