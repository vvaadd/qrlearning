package ru.vvaadd.qrlearning.qrbuilders;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.QRCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vvaadd.qrlearning.models.QRModel;
import ru.vvaadd.qrlearning.renderers.IQRRenderer;
import ru.vvaadd.qrlearning.utils.ImageUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;

public class QrCodeGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(QrCodeGenerator.class);
    // Opacity bright
    private static final int OPACITY_BRIGHT = (int) (255 * 0.75);
    // Opacity dark
    private static final int OPACITY_DARK = (int) (255 * 0.25);
    // Bright black
    private static final Color BB = new Color(0, 0, 0, OPACITY_BRIGHT);
    // Dark black
    private static final Color DB = new Color(0, 0, 0, OPACITY_DARK);
    // Bright white
    private static final Color BW = new Color(255, 255, 255, OPACITY_BRIGHT);
    // Dark white
    private static final Color DW = new Color(255, 255, 255, OPACITY_DARK);


    /**
     * Call this method to create a QR-code image.
     *
     * @param qrModel The QR-model containing size, logo, data.
     * @return BufferedImage of the pure QR-code
     * @throws WriterException, IOException If an Exception occur during the create of the QR-code or
     *                          while writing the data into the OutputStream.
     */
    public static BufferedImage createQrImage(QRModel qrModel, IQRRenderer renderer) throws WriterException, IOException {
        //String content, int qrCodeSize, BufferedImage logo
        // Correction level - HIGH - more chances to recover message
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap =
                new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        // Generate QR-code
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder();
        QRCode qrCode = qrCodeEncoder.encode(qrModel.getData(), BarcodeFormat.QR_CODE, hintMap);
        // Generate QR-code image
        BitMatrix bitMatrix = renderer.renderResult(qrCode, qrModel.getSize(), qrModel.getSize());
        // Start work with picture
        int matrixWidth = bitMatrix.getWidth();
        int matrixHeight = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(matrixWidth, matrixHeight,
                BufferedImage.TYPE_INT_ARGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
        graphics.setFont(font);
        graphics.setColor(BW);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        graphics.setColor(qrModel.getColor());
        // Write message under the QR-code
        graphics.drawString(qrModel.getData(), 30, image.getHeight() - graphics.getFont().getSize());

        //Write Bit Matrix as image
        Color col = qrModel.getColor();
        graphics.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(), OPACITY_BRIGHT));
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (bitMatrix.get(i, j)) {
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
        LOG.info("Your QR-code was succesfully generated.");
        return image;
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
