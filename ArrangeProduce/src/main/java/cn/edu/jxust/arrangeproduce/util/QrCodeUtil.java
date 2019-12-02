package cn.edu.jxust.arrangeproduce.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * @author ZSS
 * @date 2019/11/29 15:22
 * @description 二维码生成工具
 */
@Slf4j
@Component
public class QrCodeUtil {

    /**
     * 二维码宽度
     */
    private static final int QR_WIDTH = 300;
    /**
     * 二维码高度
     */
    private static final int QR_HEIGHT = 300;

    /**
     * 生成二维码
     *
     * @param content 要生成的内容
     * @return String Base64格式的地址
     */
    public static String createQrCode(String content) {
        String resultImage;
        if (!StringUtils.isEmpty(content)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            @SuppressWarnings("rawtypes")
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>(3);
            // 指定字符编码为 "utf-8"
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 指定二维码的纠错等级为中级 Middle
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // 设置图片的边距
            hints.put(EncodeHintType.MARGIN, 2);

            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);

                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, "png", os);
                /*
                 * 原生转码面前没有 data:image/png;base64 这些字段的，返回给前端无法被解析，可以让前端加，也可以在这里家
                 */
                resultImage = "data:image/png;base64," + Base64.encode(os.toByteArray());
                return resultImage;
            } catch (Exception e) {
                log.error("Create QrCode Error :{}", e.toString());
            }
        }
        return null;
    }
}
