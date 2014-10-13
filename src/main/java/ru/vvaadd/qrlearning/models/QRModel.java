package ru.vvaadd.qrlearning.models;

import java.awt.*;

/**
 * Created by vadim on 23.08.14.
 */
public class QRModel {
    private String bgimgPath;
    private String logoPath;
    private String data;
    private int size;
    private String type = "png";
    private Color color;

    public String getBgimgPath() {
        return bgimgPath;
    }

    public void setBgimgPath(String bgimgPath) {
        this.bgimgPath = bgimgPath;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
