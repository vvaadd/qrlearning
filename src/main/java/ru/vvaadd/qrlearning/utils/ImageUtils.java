package ru.vvaadd.qrlearning.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by vadim on 07.09.14.
 */
public class ImageUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ImageUtils.class);

    public static BufferedImage getImageFromFile(String filename) throws IOException {
        if(filename == null){
            return null;
        }
        return ImageIO.read(new File(filename));
    }

    /**
     * @param img            Image which should be saved
     * @param imageFormat    The image format in which the image should be rendered. As
     *                       Example 'png' or 'jpg'. See @javax.imageio.ImageIO for more
     *                       information which image formats are supported.
     * @param resultFileName File name of the result
     * @throws IOException
     */
    public static void writeQrCodeToFile(BufferedImage img, String imageFormat, String resultFileName) throws IOException {
        if (img != null) {
            ImageIO.write(img, imageFormat, new FileOutputStream(new File(resultFileName)));
            LOG.info("Your QR-code was succesfully written to file" + resultFileName);
        } else {
            LOG.warn("Can't write image [img = null]" + resultFileName);
        }
    }
}
