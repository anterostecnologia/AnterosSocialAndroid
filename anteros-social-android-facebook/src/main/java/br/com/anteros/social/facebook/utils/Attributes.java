package br.com.anteros.social.facebook.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edson on 24/03/16.
 */
public abstract class Attributes {

    protected Map<String, String> attributes = new HashMap<String, String>();

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public static PictureAttributes createPictureAttributes() {
        return new PictureAttributes();
    }
}