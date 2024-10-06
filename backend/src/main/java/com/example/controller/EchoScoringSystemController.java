package com.example.controller;

import java.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.entity.impl.EchoImpl;
import com.example.mapper.EchoMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/echo-scoring-system")
public class EchoScoringSystemController {

    @Resource
    EchoMapper mapper;

    private static final String[] KEYS = new String[]{ // 副词条名称
            "固定攻击", "固定生命", "固定防御", "百分比攻击", "百分比生命", "百分比防御",
            "暴击率", "暴击伤害", "共鸣效率", "普攻伤害加成", "重击伤害加成", "共鸣技能伤害加成", "共鸣解放伤害加成"
    };
    private static final Map<String, double[]> VALUES = Map.ofEntries( // 副词条取值
            Map.entry("固定攻击", new double[]{60, 50, 40, 30}),
            Map.entry("固定生命", new double[]{580, 540, 510, 470, 430, 390, 360, 320}),
            Map.entry("固定防御", new double[]{70, 60, 50, 40}),
            Map.entry("百分比攻击", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4}),
            Map.entry("百分比生命", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4}),
            Map.entry("百分比防御", new double[]{14.7, 13.8, 12.8, 11.8, 10.9, 10.0, 9.0, 8.1}),
            Map.entry("暴击率", new double[]{10.5, 9.9, 9.3, 8.7, 8.1, 7.5, 6.9, 6.3}),
            Map.entry("暴击伤害", new double[]{21.0, 19.8, 18.6, 17.4, 16.2, 15.0, 13.8, 12.6}),
            Map.entry("共鸣效率", new double[]{12.4, 11.6, 10.8, 10.0, 9.2, 8.4, 7.6, 6.8}),
            Map.entry("普攻伤害加成", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4}),
            Map.entry("重击伤害加成", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4}),
            Map.entry("共鸣技能伤害加成", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4}),
            Map.entry("共鸣解放伤害加成", new double[]{11.6, 10.9, 10.1, 9.4, 8.6, 7.9, 7.1, 6.4})
    );
    private static final Map<String, Double> AVERAGE = Map.ofEntries( // 副词条基准值
            Map.entry("固定攻击", 45.0),
            Map.entry("固定生命", 450.0),
            Map.entry("固定防御", 55.0),
            Map.entry("百分比攻击", 9.0),
            Map.entry("百分比生命", 9.0),
            Map.entry("百分比防御", 11.3875),
            Map.entry("暴击率", 8.4),
            Map.entry("暴击伤害", 16.8),
            Map.entry("共鸣效率", 9.6),
            Map.entry("普攻伤害加成", 9.0),
            Map.entry("重击伤害加成", 9.0),
            Map.entry("共鸣技能伤害加成", 9.0),
            Map.entry("共鸣解放伤害加成", 9.0)
    );
    private static final Map<String, int[]> BASICS = Map.ofEntries( // 角色基础三维属性
            Map.entry("长离", new int[]{1049, 10387, 1099})
    );
    private static final Map<String, int[]> WEIGTHS = Map.ofEntries( // 角色副词条权重
            Map.entry("长离", new int[]{11, 1, 1, 20, 3, 2, 3, 8, 5})
    );

    private Map<String, ? extends Number> __getWeigths(String name) { // 角色完整副词条权重
        int[] w = WEIGTHS.get(name), b = BASICS.get(name);
        return Map.ofEntries(
                Map.entry("固定攻击", w[0] * AVERAGE.get("固定攻击") * 100 / AVERAGE.get("百分比攻击") / b[0]),
                Map.entry("固定生命", w[1] * AVERAGE.get("固定生命") * 100 / AVERAGE.get("百分比生命") / b[1]),
                Map.entry("固定防御", w[2] * AVERAGE.get("固定防御") * 100 / AVERAGE.get("百分比防御") / b[2]),
                Map.entry("百分比攻击", w[0]),
                Map.entry("百分比生命", w[1]),
                Map.entry("百分比防御", w[2]),
                Map.entry("暴击率", w[3]),
                Map.entry("暴击伤害", w[3]),
                Map.entry("共鸣效率", w[4]),
                Map.entry("普攻伤害加成", w[5]),
                Map.entry("重击伤害加成", w[6]),
                Map.entry("共鸣技能伤害加成", w[7]),
                Map.entry("共鸣解放伤害加成", w[8])
        );
    }

    private Map<String, List<EchoImpl>> findData(String username) { // 查找角色词条数据
        String s = mapper.findEchoScoringSystem(username);
        Map<String, List<EchoImpl>> data;
        if (s == null) {
            data = new HashMap<>();
            mapper.updateEchoScoringSystem(username, JSON.toJSONString(data));
        } else {
            data = JSON.parseObject(s, new TypeReference<Map<String, List<EchoImpl>>>() {});
        }
        sortData(data);
        return data;
    }

    private void sortData(Map<String, List<EchoImpl>> data) { // 声骸列表排序
        for (String key: data.keySet()) {
            List<EchoImpl> list = data.get(key);
            list.sort((a, b) -> { //
                if (a.getScore().isEmpty()) return 1;
                else if (b.getScore().isEmpty()) return -1;
                if (a.getCost().equals(b.getCost()))
                    return Integer.parseInt(b.getScore()) - Integer.parseInt(a.getScore());
                else
                    return Integer.parseInt(b.getCost()) - Integer.parseInt(a.getCost());
            });
        }
    }

    @PostMapping("/get-weigths")
    public Map<String, ? extends Number> getWeigths(@RequestBody String name) { // 获取角色完整副词条权重
        name = name.substring(1, name.length() - 1);
        return __getWeigths(name);
    }

    @GetMapping("/get-values")
    public Map<String, double[]> getValues() { // 获取副词条取值
        return VALUES;
    }

    @PostMapping("/get-percent")
    public Map<String, Number> calculateScore(@RequestParam("name") String name,
                                              @RequestParam("values") String json) { // 计算声骸评分
        Map<String, Number> values = JSON.parseObject(json, new TypeReference<Map<String, Number>>() {}); // 输入副词条数据
        Map<String, ? extends Number> weigths = __getWeigths(name); // 角色完整副词条权重
        Map<String, Number> res = new LinkedHashMap<>(); // 返回值
        List<Map.Entry<String, Double>> pairs = new ArrayList<>();
        for (String key: KEYS) {
            double value = weigths.get(key).doubleValue() * VALUES.get(key)[0] / AVERAGE.get(key);
            pairs.add(new AbstractMap.SimpleEntry<>(key, value));
        }
        pairs.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        double maxScore = pairs.stream().limit(5).mapToDouble(Map.Entry::getValue).sum(); // 理论最大分数
        double myScore = 0; // 实际分数
        for (String key: values.keySet()) {
            double score = weigths.get(key).doubleValue() * values.get(key).doubleValue() / AVERAGE.get(key);
            myScore += score;
            res.put(key, score);
        }
        for (String key: values.keySet()) {
            res.put(key, (int) Math.round(res.get(key).doubleValue() * 100 / myScore)); // 每个副词条对总得分的贡献百分比
        }
        res.put("总得分", (int) Math.round(myScore * 100 / maxScore)); // 总得分
        return res;
    }

    @PostMapping("/get-data")
    public Map<String, List<EchoImpl>> getData(@RequestBody String username) { // 获取数据
        username = username.substring(1, username.length() - 1);
        return findData(username);
    }

    @PostMapping("/add-data")
    public Map<String, List<EchoImpl>> addData(@RequestParam("username") String username,
                        @RequestParam("json") String json,
                        @RequestParam("name") String name) { // 添加数据
        Map<String, List<EchoImpl>> data = findData(username);
        EchoImpl echo = JSON.parseObject(json, EchoImpl.class);
        if (!data.containsKey(name)) {
            List<EchoImpl> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                list.add(EchoImpl.getEmpty());
            }
            data.put(name, list);
        }
        List<EchoImpl> list = data.get(name);
        for (int i = 0; i < 5; i++) {
            if (list.get(i).getCost().isEmpty()) {
                list.set(i, echo);
                break;
            }
        }
        sortData(data);
        mapper.updateEchoScoringSystem(username, JSON.toJSONString(data));
        return data;
    }

    @PostMapping("/del-echo")
    public Map<String, List<EchoImpl>> delEcho(@RequestParam("username") String username,
                                               @RequestParam("index") Integer index,
                                               @RequestParam("name") String name) { // 删除数据
        Map<String, List<EchoImpl>> data = findData(username);
        List<EchoImpl> list = data.get(name);
        list.set(index, EchoImpl.getEmpty());
        for (int i = 0, cnt = 0; i < 5; i++) {
            if (list.get(i).getCost().isEmpty()) {
                cnt++;
            }
            if (cnt == 5) {
                data.remove(name);
            }
        }
        sortData(data);
        mapper.updateEchoScoringSystem(username, JSON.toJSONString(data));
        return data;
    }
}