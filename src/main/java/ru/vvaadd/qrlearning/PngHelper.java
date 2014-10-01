package ru.vvaadd.qrlearning;

import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vvaadd.qrlearning.model.QRModel;
import ru.vvaadd.qrlearning.qrbuilder.QrCodeGenerator;
import ru.vvaadd.qrlearning.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by vadim on 23.08.14.
 */
public class PngHelper {
    private static final Logger LOG = LoggerFactory.getLogger(PngHelper.class);

    public void create(QRModel model, String resultFile) throws IOException, WriterException {
//        options[:size] ||= 4
//        options[:level] ||= :h
//        options[:modified] ||= (options[:level] == :h ? 0.03 : 0)
//        options[:bgcolor] ||= ChunkyPNG::Color(255, 255, 255)
//        options[:dotpadding] ||= 0.25
//        size = size(options[:size])
        BufferedImage bgimg = ImageUtils.getImageFromFile(model.getBgimgPath());
        BufferedImage qrImage = QrCodeGenerator.createQrImage(model);
        bgimg.createGraphics();
        bgimg.getGraphics().drawImage(qrImage, 0, 0, null);

        ImageUtils.writeQrCodeToFile(bgimg, model.getType(), resultFile);
        LOG.info("done.");
    }


}
