package com.minecraft.minepay.util.qrcode;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class QrcodeGenerator {

    @SneakyThrows
    public static BufferedImage generateQrcode(String data) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(data);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);

            byteArrayInputStream.close();

            return bufferedImage;
        } catch (Exception e) {
            return null;
        }
    }

}
