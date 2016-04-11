/*
 * ******************************************************************************
 *  * Copyright 2016 Anteros Tecnologia
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package br.com.anteros.social.facebook.utils;

/**
 * Created by edson on 24/03/16.
 */

public class PictureAttributes extends Attributes {

    private final static String HEIGHT = "height";
    private final static String WIDTH = "width";
    private final static String TYPE = "type";

    PictureAttributes() {
    }

    public void setHeight(int pixels) {
        attributes.put(HEIGHT, String.valueOf(pixels));
    }

    public void setWidth(int pixels) {
        attributes.put(WIDTH, String.valueOf(pixels));
    }

    public void setType(PictureType type) {
        attributes.put(TYPE, type.getValue());
    }

    public static enum PictureType {
        SMALL("small"),
        NORMAL("normal"),
        LARGE("large"),
        SQUARE("square");

        private String mValue;

        private PictureType(String value) {
            mValue = value;
        }

        public String getValue() {
            return mValue;
        }
    }
}