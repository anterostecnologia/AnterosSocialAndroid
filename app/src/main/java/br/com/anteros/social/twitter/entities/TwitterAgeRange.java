package br.com.anteros.social.twitter.entities;

/**
 * Created by edson on 25/03/16.
 */
public class TwitterAgeRange {


    private String min;

    private String max;

    public TwitterAgeRange(String min, String max) {
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

