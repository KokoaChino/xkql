package com.example.entity.order;

import lombok.Builder;
import lombok.Getter;
import lombok.With;
import java.awt.*;


@With
@Builder
@Getter
public class WatermarkParams {

    public static final int N = 1200;

    // 位置（水印左下角的起始坐标）
    @Builder.Default private final Integer x = 0;
    @Builder.Default private final Integer y = N;

    // 字符偏移
    @Builder.Default private final Integer dx = 0;
    @Builder.Default private final Integer dy = 0;
    @Builder.Default private final Boolean includeCharWidthInDx = true;
    @Builder.Default private final Boolean includeCharHeightInDy = false;

    // 字体
    @Builder.Default private final String fontName = "微软雅黑";
    @Builder.Default private final Integer fontStyles = Font.PLAIN;
    @Builder.Default private final Integer fontSize = 100;
    @Builder.Default private final Color fontColor = Color.BLACK;

    // 描边
    @Builder.Default private final Integer strokeSize = null;
    @Builder.Default private final Color strokeColor = Color.WHITE;

    // 渐变
    @Builder.Default private final GradientParams fontGradient = null;
    @Builder.Default private final GradientParams strokeGradient = null;

    // 仿射变换
    @Builder.Default private final Float rotation = Float.NaN;
    @Builder.Default private final Float shearX = Float.NaN;
    @Builder.Default private final Boolean priorityRotation = true;

    @With
    @Builder
    @Getter
    public static class GradientParams {
        @Builder.Default private final Point start = new Point(0, 0);
        @Builder.Default private final Color startColor = Color.BLUE;
        @Builder.Default private final Point end = new Point(N, N);
        @Builder.Default private final Color endColor = Color.RED;
        @Builder.Default private final Boolean cyclic = true;
    }
}