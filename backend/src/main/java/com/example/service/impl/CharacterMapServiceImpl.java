package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.mapper.CharacterMapper;
import com.example.service.api.CharacterMapService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class CharacterMapServiceImpl implements CharacterMapService {

//    private static final Map<String, Integer> SHOW = new HashMap<>();
//    static {
//        SHOW.put("保登摩卡", 3);
//        SHOW.put("保登心爱", 565);
//        SHOW.put("豆馅", 4);
//        SHOW.put("奈津惠", 72);
//        SHOW.put("青山蓝山", 23);
//        SHOW.put("塞西莉亚", 41);
//        SHOW.put("提比", 34);
//        SHOW.put("天天座理世", 48);
//        SHOW.put("条河麻耶", 10);
//        SHOW.put("桐间纱路", 154);
//        SHOW.put("香风智乃", 101);
//        SHOW.put("野雁", 6);
//        SHOW.put("宇治松千夜", 188);
//        SHOW.put("宇佐美瑞希", 1);
//    }

//    @Override
//    public List<List<String>> getCharacterMapCatalogue() {
//        Path rootPath = Paths.get("").toAbsolutePath().getParent();
//        File characterMap = new File(rootPath.toString() + "/xkql/frontend/public/角色图");
//        File[] files = characterMap.listFiles();
//        List<List<String>> res = new ArrayList<>();
//        if (files != null) {
//            for (File file : files) {
//                res.add(new ArrayList<>(List.of(file.getName(), Objects.requireNonNull(file.listFiles())[SHOW.get(file.getName()) - 1].getName())));
//                List<String> last = res.get(res.size() - 1);
//                last.add("/角色图压缩/" + last.get(0) + ".zip");
//                last.add(last.get(0) + ".zip");
//            }
//        }
//        return res;
//    }

//    @Override
//    public List<List<String>> getCharacterMapFiles(String name) {
//        name = name.substring(1, name.length() - 1);
//        Path rootPath = Paths.get("").toAbsolutePath().getParent();
//        File characterMap = new File(rootPath.toString() + "/xkql/frontend/public/角色图/" + name);
//        File[] files = characterMap.listFiles();
//        List<List<String>> res = new ArrayList<>();
//        if (files != null) {
//            for (File file : files) {
//                double size = file.length() / 1024.0;
//                String fileName = file.getName();
//                fileName = fileName.substring(0, fileName.lastIndexOf('.'));
//                res.add(new ArrayList<>(List.of(fileName, String.format("%.2f KB", size), file.getName())));
//            }
//        }
//        return res;
//    }

    @Resource
    CharacterMapper mapper;

    private static final List<List<String>> CHARACTER_MAP_CATALOGUE = List.of(
            Arrays.asList("保登心爱", "保登心爱 565.jpg", "/角色图压缩/保登心爱.zip", "保登心爱.zip"),
            Arrays.asList("保登摩卡", "保登摩卡 03.jpg", "/角色图压缩/保登摩卡.zip", "保登摩卡.zip"),
            Arrays.asList("塞西莉亚", "塞西莉亚 41.jpg", "/角色图压缩/塞西莉亚.zip", "塞西莉亚.zip"),
            Arrays.asList("天天座理世", "天天座理世 048.jpg", "/角色图压缩/天天座理世.zip", "天天座理世.zip"),
            Arrays.asList("奈津惠", "奈津惠 72.jpg", "/角色图压缩/奈津惠.zip", "奈津惠.zip"),
            Arrays.asList("宇佐美瑞希", "宇佐美瑞希 01.jpg", "/角色图压缩/宇佐美瑞希.zip", "宇佐美瑞希.zip"),
            Arrays.asList("宇治松千夜", "宇治松千夜 188.jpg", "/角色图压缩/宇治松千夜.zip", "宇治松千夜.zip"),
            Arrays.asList("提比", "提比 34.jpg", "/角色图压缩/提比.zip", "提比.zip"),
            Arrays.asList("条河麻耶", "条河麻耶 10.jpg", "/角色图压缩/条河麻耶.zip", "条河麻耶.zip"),
            Arrays.asList("桐间纱路", "桐间纱路 154.jpg", "/角色图压缩/桐间纱路.zip", "桐间纱路.zip"),
            Arrays.asList("豆馅", "豆馅 4.jpg", "/角色图压缩/豆馅.zip", "豆馅.zip"),
            Arrays.asList("野雁", "野雁 6.jpg", "/角色图压缩/野雁.zip", "野雁.zip"),
            Arrays.asList("青山蓝山", "青山蓝山 23.jpg", "/角色图压缩/青山蓝山.zip", "青山蓝山.zip"),
            Arrays.asList("香风智乃", "香风智乃 101.jpg", "/角色图压缩/香风智乃.zip", "香风智乃.zip")
    );
    private static final Map<String, List<List<String>>> CHARACTER_MAP_FILES = new HashMap<>();

    @Override
    public List<List<String>> getCharacterMapCatalogue() {
        return CHARACTER_MAP_CATALOGUE;
    }

    @Override
    public List<List<String>> getCharacterMapFiles(String name) {
        if (!CHARACTER_MAP_FILES.containsKey(name)) {
            String json = mapper.selectByName(name);
            List<List<String>> value = JSON.parseObject(json, new TypeReference<List<List<String>>>() {});
            CHARACTER_MAP_FILES.put(name, value);
        }
        return CHARACTER_MAP_FILES.get(name);
    }
}