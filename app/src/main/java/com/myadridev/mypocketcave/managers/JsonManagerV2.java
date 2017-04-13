package com.myadridev.mypocketcave.managers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.Type;

public class JsonManagerV2 {

    private static final Gson json = new GsonBuilder().enableComplexMapKeySerialization().create();

    public static String writeValueAsString(Object value) {
        String json = JsonManagerV2.json.toJson(value);
        return json;
    }

    public static <T> T readValue(String dataJson, Type dataType) {
        try {
            return json.fromJson(dataJson, dataType);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
