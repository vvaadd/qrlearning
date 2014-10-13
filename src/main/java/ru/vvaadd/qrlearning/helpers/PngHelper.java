package ru.vvaadd.qrlearning.helpers;

import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vvaadd.qrlearning.models.QRModel;
import ru.vvaadd.qrlearning.qrbuilders.QrCodeGenerator;
import ru.vvaadd.qrlearning.renderers.IQRRenderer;
import ru.vvaadd.qrlearning.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by vadim on 23.08.14.
 */
public class PngHelper {
    private static final Logger LOG = LoggerFactory.getLogger(PngHelper.class);

    public void create(QRModel model, String resultFile, IQRRenderer renderer) throws IOException, WriterException {
        BufferedImage resimg = ImageUtils.getImageFromFile(model.getBgimgPath());
        BufferedImage qrImage = QrCodeGenerator.createQrImage(model, renderer);
        resimg.createGraphics();
        resimg.getGraphics().drawImage(qrImage, 0, 0, null);

        ImageUtils.writeQrCodeToFile(resimg, model.getType(), resultFile);
        LOG.info("done.");
    }


}
