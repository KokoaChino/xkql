package com.example.util;

import com.example.entity.order.WatermarkParams;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class ImageUtil {

    public static final java.util.List<WatermarkParams> P = List.of(
            WatermarkParams.builder()
                    .x(90).y(1165).dx(-10)
                    .fontName("Source Han Sans CN").fontSize(130).fontColor(new Color(254, 249, 233))
                    .build(),
            WatermarkParams.builder()
                    .x(90).y(1165).dx(-10)
                    .fontName("Source Han Sans CN").fontSize(130)
                    .fontGradient(WatermarkParams.GradientParams.builder()
                            .startColor(new Color(254, 249, 233))
                            .end(new Point(80, 80))
                            .endColor(new Color(220, 176, 119))
                            .build())
                    .build(),
            WatermarkParams.builder()
                    .x(333).y(510).dy(-3)
                    .fontStyles(Font.BOLD | Font.ITALIC).fontSize(55).fontColor(Color.WHITE)
                    .strokeSize(5).strokeColor(new Color(250, 65, 10))
                    .build(),
            WatermarkParams.builder()
                    .x(70).y(705).dx(-8)
                    .fontName("DIN").fontStyles(Font.BOLD).fontColor(new Color(227, 34, 17))
                    .build(),
            WatermarkParams.builder()
                    .x(840).y(1175).dx(-3)
                    .fontName("HarmonyOS Sans SC").fontStyles(Font.BOLD | Font.ITALIC).fontSize(140).fontColor(new Color(0xfdf04a))
                    .strokeSize(6).strokeColor(new Color(0x065223))
                    .build()
    );

    public static void handleImage(int mode, File file, String watermark, OutputStream outputStream) { // 处理图片
        try {
            Image image = ImageIO.read(file);
            BufferedImage bufferedImage = handleWatermark(mode, image, watermark);
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage handleWatermark(int mode, Image image, String watermark) { // 处理图片水印
        WatermarkParams p;
        try {
            p = P.get(mode - 1);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException("模式设置错误！");
        }
        int width = image.getWidth(null), height = image.getHeight(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawImage(image, 0, 0, width, height, null);
        g.setFont(new Font(p.getFontName(), p.getFontStyles(), p.getFontSize())); // 字体
        FontMetrics fm = g.getFontMetrics();
        if (!Float.isNaN(p.getRotation()) || !Float.isNaN(p.getShearX())) { // 仿射变换
            g.transform(getAffineTransform(p));
        }
        if (p.getStrokeSize() != null) { // 描边
            if (p.getStrokeGradient() != null) {
                WatermarkParams.GradientParams gp = p.getStrokeGradient();
                g.setPaint(new GradientPaint(gp.getStart(), gp.getStartColor(), gp.getEnd(), gp.getEndColor(), gp.getCyclic()));
            } else {
                g.setColor(p.getStrokeColor());
            }
            for (int i = 0, x = p.getX(), y = p.getY(), gap = p.getStrokeSize(); i < watermark.length(); i++) {
                char c = watermark.charAt(i);
                for (int j = -gap; j <= gap; j++) {
                    for (int k = -gap; k <= gap; k++) {
                        g.drawString(String.valueOf(c), x + j, y + k);
                    }
                }
                x += (p.getIncludeCharWidthInDx() ? fm.charWidth(c) : 0) + p.getDx();
                y += (p.getIncludeCharHeightInDy() ? fm.getAscent() : 0) + p.getDy();
            }
        }
        if (p.getFontGradient() != null) {
            WatermarkParams.GradientParams gp = p.getFontGradient();
            g.setPaint(new GradientPaint(gp.getStart(), gp.getStartColor(), gp.getEnd(), gp.getEndColor(), gp.getCyclic()));
        } else {
            g.setColor(p.getFontColor());
        }
        for (int i = 0, x = p.getX(), y = p.getY(); i < watermark.length(); i++) {
            char c = watermark.charAt(i);
            g.drawString(String.valueOf(c), x, y);
            x += (p.getIncludeCharWidthInDx() ? fm.charWidth(c) : 0) + p.getDx();
            y += (p.getIncludeCharHeightInDy() ? fm.getAscent() : 0) + p.getDy();
        }
        g.dispose();
        return bufferedImage;
    }

    private static AffineTransform getAffineTransform(WatermarkParams p) { // 获取仿射变换
        AffineTransform transform = new AffineTransform();
        if (!Float.isNaN(p.getRotation()) && !Float.isNaN(p.getShearX())) {
            if (p.getPriorityRotation()) {
                transform.rotate(p.getRotation());
                transform.shear(p.getShearX(), 0);
            } else {
                transform.shear(p.getShearX(), 0);
                transform.rotate(p.getRotation());
            }
        } else if (!Float.isNaN(p.getRotation())) {
            transform.rotate(p.getRotation());
        } else {
            transform.shear(p.getShearX(), 0);
        }
        return transform;
    }

    public static boolean check(String targetFont) { // 检查系统是否安装有目标字体
        String[] fontNames =  GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Set<String> fontList = Arrays.stream(fontNames).collect(Collectors.toSet());
        return fontList.contains(targetFont);
    }
}