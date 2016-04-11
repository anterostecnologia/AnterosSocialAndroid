package br.com.anteros.social.facebook.entities;

import com.google.gson.annotations.SerializedName;

import br.com.anteros.social.core.AgeRange;

/**
 * Created by edson on 24/03/16.
 */
public class FacebookAgeRange implements AgeRange {

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