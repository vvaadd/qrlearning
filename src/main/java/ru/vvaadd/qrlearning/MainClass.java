package ru.vvaadd.qrlearning;

import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vvaadd.qrlearning.helpers.PngHelper;
import ru.vvaadd.qrlearning.models.QRModel;
import ru.vvaadd.qrlearning.renderers.IQRRenderer;
import ru.vvaadd.qrlearning.renderers.QRSimpleRenderer;

import java.awt.*;
import java.io.IOException;

/**
 * Created by vadim on 30.06.14.
 */
public class MainClass {
    private static final Logger LOG = LoggerFactory.getLogger(MainClass.class);

    private final static String resultFile = "/home/vadim/IdeaProjects/temp/result.png";
    private final static String logoFile = "/home/vadim/IdeaProjects/temp/test.png";
    private final static String bgImg = "/home/vadim/IdeaProjects/temp/wsurf.png";
    private final static String data = "http://blabla.ru";

    public static void main(String[] args) {
        QRModel model = new QRModel();
        model.setLogoPath(logoFile);
        model.setBgimgPath(bgImg);
        model.setData(data);
        model.setSize(300);
        model.setColor(new Color(51, 102, 153, 100));

        PngHelper pngHelper = new PngHelper();
        IQRRenderer renderer = new QRSimpleRenderer();
        try {
            pngHelper.create(model, resultFile, renderer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        LOG.info("done.");
    }
}
