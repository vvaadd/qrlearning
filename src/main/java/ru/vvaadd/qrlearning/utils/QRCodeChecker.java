package ru.vvaadd.qrlearning.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.Collections;

/**
 * Created by vadim on 14.10.14.
 */
public class QRCodeChecker {
    private static final Logger LOG = LoggerFactory.getLogger(QRCodeChecker.class);

    public static boolean isQRCodeCorrect(String content, BufferedImage image) {
        boolean result = false;
        Result qrResult = decode(image);
        if (qrResult != null && content != null && content.equals(qrResult.getText())) {
            result = true;
        }
        return result;
    }

    private static Result decode(BufferedImage image) {
        if (image == null) {
            return null;
        }
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            return new MultiFormatReader().decode(bitmap, Collections.<DecodeHintType, Object>emptyMap());
        } catch (NotFoundException nfe) {
            LOG.warn("Exception during checking image.", nfe);
            return null;
        }
    }
}
