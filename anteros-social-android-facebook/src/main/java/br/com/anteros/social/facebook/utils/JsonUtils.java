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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by edson on 23/03/16.
 */
public class JsonUtils {


    private static Gson buildGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        return gson;
    }

    public static String toJson(Object obj) {
        Gson gson = buildGson();
        return gson.toJson(obj);
    }


    public static <T> T fromJson(String json, Class<T> cls) {
        Gson gson = buildGson();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);

        if (cls != null && cls.isArray() && element instanceof JsonArray == false) {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(element);

            Type listType = new TypeToken<T>() {
            }.getType();
            return gson.fromJson(jsonArray, listType);
        }

        return gson.fromJson(json, cls);
    }


    public static <T> T fromJson(byte[] json, Class<T> cls) {
        try {
            String decoded = new String(json, "UTF-8");
            return fromJson(decoded, cls);
        } catch (Exception e) {
            return null;
        }
    }


    public static <T> T fromJson(byte[] bytes, Type type) {
        try {
            String decoded = new String(bytes, "UTF-8");
            return fromJson(decoded, type);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromJson(String json, Type type) {
        Gson gson = buildGson();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);

        return gson.fromJson(element, type);
    }


    public static <T> T fromJsonExcludeFields(String json, Class<T> cls) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);

        if (cls.isArray() && element instanceof JsonArray == false) {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(element);

            Type listType = new TypeToken<T>() {
            }.getType();
            return gson.fromJson(jsonArray, listType);
        }

        return gson.fromJson(json, cls);
    }

    private static class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        private static List<DateFormat> formats;

        {
            formats = new ArrayList<>();
            formats.add(createDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            formats.add(createDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
            formats.add(createDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
            formats.add(createDateFormat("yyyy-MM-dd"));
        }

        private static DateFormat createDateFormat(String format) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat;
        }

        private DateTypeAdapter() {
        }

        @Override
        public synchronized JsonElement serialize(Date date, Type type,
                                                  JsonSerializationContext jsonSerializationContext) {
            for (DateFormat dateFormat : formats) {
                try {
                    return new JsonPrimitive(dateFormat.format(date));
                } catch (Exception e) {
                }
            }

            return null;
        }

        @Override
        public synchronized Date deserialize(JsonElement jsonElement, Type type,
                                             JsonDeserializationContext jsonDeserializationContext) {
            Exception le = null;
            String dateString = jsonElement.getAsString();
            for (DateFormat dateFormat : formats) {
                try {
                    return dateFormat.parse(dateString);
                } catch (Exception e) {
                    le = e;
                }
            }
            throw new JsonParseException(le);
        }
    }
}
