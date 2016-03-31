package br.com.anteros.social.google;

/**
 * Created by edson on 25/03/16.
 */
public class AnterosGoogleConfiguration {



    private AnterosGoogleConfiguration(Builder builder) {

    }


    public static class Builder {


        public Builder() {
        }



        /**
         * Build the configuration for storage tool.
         *
         * @return
         */
        public AnterosGoogleConfiguration build() {
            return new AnterosGoogleConfiguration(this);
        }

    }


}
