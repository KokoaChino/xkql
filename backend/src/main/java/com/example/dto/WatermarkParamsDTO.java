package com.example.dto;

import com.example.convert.WatermarkParamsConvert;
import com.example.entity.other.WatermarkParams;
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
    @Builder.Default private final Integer strokeSize = 0;
    @Builder.Default private final String strokeColor = WatermarkParamsConvert.getColor(Color.WHITE);

    // 投影
    @Builder.Default private final Integer shadowDx = 0;
    @Builder.Default private final Integer shadowDy = 0;
    @Builder.Default private final String shadowColor = WatermarkParamsConvert.getColor(Color.BLACK);
    @Builder.Default private final Float shadowOpacity = 0f;

    // 渐变
    @NotNull private final gradientDTO fontGradient;
    @NotNull private final gradientDTO strokeGradient;

    // 仿射变换
    @Builder.Default private final Integer rotation = 0;
    @Builder.Default private final Float shearX = 0f;
    @Builder.Default private final Boolean priorityRotation = true;

    @With
    @Builder
    @Getter
    public static final class gradientDTO {
        @Builder.Default Boolean enableGradient = false;
        @Builder.Default private final Point start = new Point(0, 0);
        @Builder.Default private final String startColor = WatermarkParamsConvert.getColor(Color.BLUE);
        @Builder.Default private final Point end = new Point(WatermarkParams.N, WatermarkParams.N);
        @Builder.Default private final String endColor = WatermarkParamsConvert.getColor(Color.RED);
        @Builder.Default private final Boolean cyclic = true;
    }
}