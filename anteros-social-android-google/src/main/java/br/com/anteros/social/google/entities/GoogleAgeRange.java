package br.com.anteros.social.google.entities;

import br.com.anteros.social.core.AgeRange;

/**
 * Created by edson on 25/03/16.
 */
public class GoogleAgeRange implements AgeRange{


    private String min;

    private String max;

    public GoogleAgeRange(String min, String max) {
        this.min = min;
        this.max = max;
    }


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

