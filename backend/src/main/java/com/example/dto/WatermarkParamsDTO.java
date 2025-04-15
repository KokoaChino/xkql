package com.example.dto;

import com.example.converter.WatermarkParamsConverter;
import com.example.entity.order.WatermarkParams;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import java.awt.*;
import java.util.List;


@With
@Builder
@Getter
public class WatermarkParamsDTO {

    // 位置（水印左下角的起始坐标）
    @NotNull private final Integer x, y;

    // 字符偏移
    @NotNull private final Integer dx, dy;
    @NotNull private final Boolean includeCharWidthInDx, includeCharHeightInDy;

    // 字体
    @NotNull private final String fontName;
    @NotNull private final List<String> fontStyles;
    @NotNull private final Integer fontSize;
    @NotNull private final String fontColor;

    // 描边
    @Builder.Default private final Integer strokeSize = null;
    @Builder.Default private final String strokeColor = WatermarkParamsConverter.getColor(Color.WHITE);

    // 渐变
    @NotNull private final gradientDTO fontGradient;
    @NotNull private final gradientDTO strokeGradient;

    // 仿射变换
    @Builder.Default private final Float rotation = Float.NaN;
    @Builder.Default private final Float shearX = Float.NaN;
    @Builder.Default private final Boolean priorityRotation = true;

    @With
    @Builder
    @Getter
    public static final class gradientDTO {
        @Builder.Default Boolean enableGradient = false;
        @Builder.Default private final Point start = new Point(0, 0);
        @Builder.Default private final String startColor = WatermarkParamsConverter.getColor(Color.BLUE);
        @Builder.Default private final Point end = new Point(WatermarkParams.N, WatermarkParams.N);
        @Builder.Default private final String endColor = WatermarkParamsConverter.getColor(Color.RED);
        @Builder.Default private final Boolean cyclic = true;
    }
}