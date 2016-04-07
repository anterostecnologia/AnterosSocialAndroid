package br.com.anteros.social.instagram;

/**
 * Created by edson on 05/04/16.
 */
public class AnterosInstagramConfiguration {



    private AnterosInstagramConfiguration(Builder builder) {

    }


    public static class Builder {


        public Builder() {
        }



        /**
         * Build the configuration for storage tool.
         *
         * @return
         */
        public AnterosInstagramConfiguration build() {
            return new AnterosInstagramConfiguration(this);
        }

    }
}
