package br.com.anteros.social.facebook.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by edson on 24/03/16.
 */
public class FacebookAgeRange {

    @SerializedName("min")
    private String min;

    @SerializedName("max")
    private String max;

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    @Override
    public String toString() {
        return min+" to "+max;
    }
}