package com.example.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.dto.WatermarkDataDTO;
import com.example.dto.WatermarkParamsDTO;
import com.example.entity.other.WatermarkData;
import com.example.entity.other.WatermarkParams;
import java.awt.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;


public class WatermarkParamsConvert {

    public static String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] toBytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    public static WatermarkDataDTO toDTO(WatermarkData w) {
        return new WatermarkDataDTO(
                w.getId(), w.getUsername(), toBase64(w.getBackgroundImage()), toBase64(w.getPreviewImage()),
                JSON.parseObject(w.getJson(), new TypeReference<WatermarkParamsDTO>() {})
        );
    }

    public static WatermarkData toEntity(WatermarkDataDTO w) {
        return new WatermarkData(
                w.getId(), w.getUsername(), toBytes(w.getBackgroundImage()), toBytes(w.getPreviewImage()),
                JSON.toJSONString(w.getParams())
        );
    }

    public static WatermarkParamsDTO toDTO(WatermarkParams w) {
        return WatermarkParamsDTO.builder()
                .x(w.getX()).y(w.getY())
                .dx(w.getDx()).dy(w.getDy())
                .includeCharWidthInDx(w.getIncludeCharWidthInDx()).includeCharHeightInDy(w.getIncludeCharHeightInDy())
                .fontName(w.getFontName()).fontStyles(getFontStyles(w.getFontStyles()))
                .fontSize(w.getFontSize()).fontColor(getColor(w.getFontColor()))
                .shadowDx(w.getShadowDx()).shadowDy(w.getShadowDy())
                .shadowColor(getColor(w.getShadowColor())).shadowOpacity(w.getShadowOpacity())
                .strokeSize(w.getStrokeSize()).strokeColor(getColor(w.getStrokeColor()))
                .fontGradient(getGradientDTO(w.getFontGradient())).strokeGradient(getGradientDTO(w.getStrokeGradient()))
                .rotation(getRotation(w.getRotation())).shearX(w.getShearX()).priorityRotation(w.getPriorityRotation())
                .build();
    }

    public static WatermarkParams toEntity(WatermarkParamsDTO w) {
        return WatermarkParams.builder()
                .x(w.getX()).y(w.getY())
                .dx(w.getDx()).dy(w.getDy())
                .includeCharWidthInDx(w.getIncludeCharWidthInDx()).includeCharHeightInDy(w.getIncludeCharHeightInDy())
                .fontName(w.getFontName()).fontStyles(getFontStyles(w.getFontStyles()))
                .fontSize(w.getFontSize()).fontColor(getColor(w.getFontColor()))
                .strokeSize(w.getStrokeSize()).strokeColor(getColor(w.getStrokeColor()))
                .shadowDx(w.getShadowDx()).shadowDy(w.getShadowDy())
                .shadowColor(getColor(w.getShadowColor())).shadowOpacity(w.getShadowOpacity())
                .fontGradient(getGradient(w.getFontGradient())).strokeGradient(getGradient(w.getStrokeGradient()))
                .rotation(getRotation(w.getRotation())).shearX(w.getShearX()).priorityRotation(w.getPriorityRotation())
                .build();
    }


    public static Integer getRotation(Float rotation) {
        return Math.toIntExact(Math.round(rotation * 180 / Math.PI));
    }

    public static Float getRotation(Integer rotation) {
        return (float) (rotation * Math.PI / 180);
    }

    public static WatermarkParamsDTO.gradientDTO getGradientDTO(WatermarkParams.GradientParams g) {
        if (Objects.isNull(g)) return WatermarkParamsDTO.gradientDTO.builder().build();
        return WatermarkParamsDTO.gradientDTO.builder()
                .enableGradient(true)
                .start(g.getStart()).startColor(getColor(g.getStartColor()))
                .end(g.getEnd()).endColor(getColor(g.getEndColor()))
                .cyclic(g.getCyclic())
                .build();
    }

    public static WatermarkParams.GradientParams getGradient(WatermarkParamsDTO.gradientDTO g) {
        if (!g.getEnableGradient()) return null;
        return WatermarkParams.GradientParams.builder()
                .start(g.getStart()).startColor(getColor(g.getStartColor()))
                .end(g.getEnd()).endColor(getColor(g.getEndColor()))
                .cyclic(g.getCyclic())
                .build();
    }

    public static String getColor(Color color) {
        return String.format("#%02X%02X%02X",
                color.getRed(), color.getGreen(), color.getBlue()
        );
    }

    public static Color getColor(String color) {
        String hex = color.startsWith("#") ? color.substring(1) : color;
        if (hex.length() != 6) {
            throw new IllegalArgumentException("颜色格式应为 #RRGGBB");
        }
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new Color(r, g, b);
    }

    public static List<String> getFontStyles(int fontStyles) {
        List<String> res = new ArrayList<>();
        if ((fontStyles & Font.BOLD) != 0) res.add("粗体");
        if ((fontStyles & Font.ITALIC) != 0) res.add("斜体");
        return res;
    }

    public static int getFontStyles(List<String> fontStyles) {
        int res = 0;
        for (String s : fontStyles) {
            if (Objects.equals(s, "粗体")) res |= Font.BOLD;
            if (Objects.equals(s, "斜体")) res |= Font.ITALIC;
        }
        return res;
    }
}