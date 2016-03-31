package br.com.anteros.social.facebook.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by edson on 24/03/16.
 */
public class FacebookImage {

    private static final String HEIGHT = "height";
    private static final String NULL = "null";
    private static final String SOURCE = "source";
    private static final String SRC = "src";
    private static final String URL = "url";
    private static final String WIDTH = "width";

    @SerializedName(HEIGHT)
    private Integer height;

    @SerializedName(SOURCE)
    private String source;

    @SerializedName(WIDTH)
    private Integer width;

    @SerializedName(URL)
    private String url;

    public Integer getHeight() {
        return height;
    }

    public String getSource() {
        return source;
    }

    public Integer getWidth() {
        return width;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url;
    }
}
