package ru.vvaadd.qrlearning.qrbuilder;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vvaadd.qrlearning.model.QRModel;
import ru.vvaadd.qrlearning.utils.ImageUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;

public class QrCodeGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(QrCodeGenerator.class);

    // Bright black
    private static final Color BB = new Color(0, 0, 0, (int) (255 * 0.25));
    // Dark black
    private static final Color DB = new Color(0, 0, 0, (int) (255 * 0.75));
    // Bright white
    private static final Color BW = new Color(255, 255, 255, (int) (255 * 0.25));
    // Dark white
    private static final Color DW = new Color(255, 255, 255, (int) (255 * 0.75));

    /**
     * Call this method to create a QR-code image.
     *
     * @param qrModel The QR-model containing size, logo, data.
     * @return BufferedImage of the pure QR-code
     * @throws WriterException, IOException If an Exception occur during the create of the QR-code or
     *                          while writing the data into the OutputStream.
     */
    public static BufferedImage createQrImage(QRModel qrModel) throws WriterException, IOException {
        //String content, int qrCodeSize, BufferedImage logo
        // Correction level - HIGH - more chances to recover message
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap =
                new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        // Generate QR-code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrModel.getData(),
                BarcodeFormat.QR_CODE, qrModel.getSize(), qrModel.getSize(), hintMap);

        // Start work with picture
        int matrixWidth = bitMatrix.getWidth();
        int matrixHeight = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(matrixWidth, matrixHeight,
                BufferedImage.TYPE_INT_ARGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
        graphics.setFont(font);
        graphics.setColor(new Color(255, 255, 255, 150));
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        graphics.setColor(qrModel.getColor());
        // Write message under the QR-code
        // TODO is it necessary?
        graphics.drawString(qrModel.getData(), 30, image.getHeight() - graphics.getFont().getSize());

        //Write Bit Matrix as image
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (bitMatrix.get(i, j)) {
                    Color col = qrModel.getColor();
                    graphics.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), (int) (255 * 0.75)));
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }

        //scale logo image and insert it to center of QR-code
        BufferedImage logo = ImageUtils.getImageFromFile(qrModel.getLogoPath());
        double scale = calcScaleRate(image, logo);
        logo = getScaledImage(logo,
                (int) (logo.getWidth() * scale),
                (int) (logo.getHeight() * scale));
        graphics.drawImage(logo,
                image.getWidth() / 2 - logo.getWidth() / 2,
                image.getHeight() / 2 - logo.getHeight() / 2,
                image.getWidth() / 2 + logo.getWidth() / 2,
                image.getHeight() / 2 + logo.getHeight() / 2,
                0, 0, logo.getWidth(), logo.getHeight(), null);
        // Check correctness of QR-code
//        if (isQRCodeCorrect(qrModel.getData(), image)) {
        LOG.info("Your QR-code was succesfully generated.");
        return image;
//        } else {
//            LOG.warn("Sorry, your logo has broke QR-code. ");
//            return null;
//
//        }
    }

    private static double calcScaleRate(BufferedImage image, BufferedImage logo) {
        double scaleRate = logo.getWidth() / image.getWidth();
        if (scaleRate > 0.3) {
            scaleRate = 0.3;
        } else {
            scaleRate = 1;
        }
        return scaleRate;
    }

    private static boolean isQRCodeCorrect(String content, BufferedImage image) {
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
            return new MultiFormatReader().decode(bitmap, Collections.EMPTY_MAP);
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            return null;
        }
    }

    private static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double) width / imageWidth;
        double scaleY = (double) height / imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(
                scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(
                image,
                new BufferedImage(width, height, image.getType()));
    }
}
