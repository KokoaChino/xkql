package com.example.util;

import com.example.entity.other.WatermarkParams;
import com.example.exception.FontNotFoundException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class ImageUtil {

    public static final Set<String> SYSTEM_FONT_SET = Arrays.stream(
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()
    ).collect(Collectors.toSet());
    public static final String UPLOAD_FONT_PATH = "backend/static/字体";
    public static final Map<String, String> UPLOAD_FONT_MAP = new HashMap<>();
    public static final Set<String> FONT_TYPE = new HashSet<>(Arrays.asList("ttf", "otf"));
    static {
        File[] files = new File(UPLOAD_FONT_PATH).listFiles();
        for (File file : files) {
            String name = file.getName();
            if (!name.contains(".")) continue;
            String type = name.substring(name.lastIndexOf(".") + 1);
            name = name.substring(0, name.lastIndexOf("."));
            if (FONT_TYPE.contains(type.toLowerCase())) {
                UPLOAD_FONT_MAP.put(name, type);
            }
        }
    }

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
                    .fontName("HarmonyOS Sans").fontStyles(Font.BOLD | Font.ITALIC).fontSize(140).fontColor(new Color(0xfdf04a))
                    .strokeSize(6).strokeColor(new Color(0x065223))
                    .build()
    );

    public static void handleImage(WatermarkParams params, File file, String watermark, OutputStream outputStream) throws FontNotFoundException { // 处理图片
        try {
            Image image = ImageIO.read(file);
            BufferedImage watermarkedImage = handleWatermark(params, image, watermark);
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) throw new RuntimeException("未找到 JPG 编写器");
            ImageWriter writer = writers.next();
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream)) {
                writer.setOutput(ios);
                JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
                jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                jpegParams.setCompressionQuality(0.98f);
                jpegParams.setOptimizeHuffmanTables(true);
                writer.write(null, new IIOImage(watermarkedImage, null, null), jpegParams);
            }
            writer.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage handleWatermark(WatermarkParams p, Image image, String watermark) throws IOException, FontNotFoundException { // 处理图片水印
        int width = image.getWidth(null), height = image.getHeight(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.drawImage(image, 0, 0, width, height, null);
        Font font = getFont(p.getFontName(), p.getFontStyles(), p.getFontSize());
        if (font == null) throw new FontNotFoundException("字体 [" + p.getFontName() + "] 未安装在系统中");
        g.setFont(font); // 字体
        FontMetrics fm = g.getFontMetrics();
        if (p.getRotation() != 0 || p.getShearX() != 0) { // 仿射变换
            g.transform(getAffineTransform(p));
        }
        if (p.getShadowOpacity() != 0) { // 投影
            Composite originalComposite = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, p.getShadowOpacity()));
            g.setColor(p.getShadowColor());
            int x = p.getX() + p.getShadowDx(), y = p.getY() + p.getShadowDy();
            for (int i = 0; i < watermark.length(); i++) {
                char c = watermark.charAt(i);
                g.drawString(String.valueOf(c), x, y);
                x += (p.getIncludeCharWidthInDx() ? fm.charWidth(c) : 0) + p.getDx();
                y += (p.getIncludeCharHeightInDy() ? fm.getAscent() : 0) + p.getDy();
            }
            g.setComposite(originalComposite);
        }
        if (p.getStrokeSize() != 0) { // 描边
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
        if (p.getFontGradient() != null) { // 渐变
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
        if (p.getRotation() != 0 && p.getShearX() != 0) {
            if (p.getPriorityRotation()) {
                transform.rotate(p.getRotation());
                transform.shear(p.getShearX(), 0);
            } else {
                transform.shear(p.getShearX(), 0);
                transform.rotate(p.getRotation());
            }
        } else if (p.getRotation() != 0) {
            transform.rotate(p.getRotation());
        } else {
            transform.shear(p.getShearX(), 0);
        }
        return transform;
    }

    public static Font getFont(String fontName, int fontStyles, int fontSize) { // 获取目标字体对象
        if (fontName.isEmpty()) fontName = "Arial";
        if (fontName.contains(".")) fontName = fontName.substring(0, fontName.lastIndexOf("."));
        if (SYSTEM_FONT_SET.contains(fontName)) {
            return new Font(fontName, fontStyles, fontSize);
        }
        if (UPLOAD_FONT_MAP.containsKey(fontName)) {
            String fileName = fontName + "." + UPLOAD_FONT_MAP.get(fontName);
            File fontFile = new File(UPLOAD_FONT_PATH, fileName);
            try {
                return Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(fontStyles, fontSize);
            } catch (FontFormatException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}