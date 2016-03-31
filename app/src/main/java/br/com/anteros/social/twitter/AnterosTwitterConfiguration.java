package br.com.anteros.social.twitter;

/**
 * Created by edson on 25/03/16.
 */
public class AnterosTwitterConfiguration {



    private AnterosTwitterConfiguration(Builder builder) {

    }


    public static class Builder {


        public Builder() {
        }



        /**
         * Build the configuration for storage tool.
         *
         * @return
         */
        public AnterosTwitterConfiguration build() {
            return new AnterosTwitterConfiguration(this);
        }

    }


}
