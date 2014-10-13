package ru.vvaadd.qrlearning.renderers;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

/**
 * Created by vadim on 14.10.14.
 */
public interface IQRRenderer {
    public static final int QUIET_ZONE_SIZE = 4;

    public BitMatrix renderResult(QRCode code, int width, int height);
}
