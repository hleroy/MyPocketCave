package com.myadridev.mypocketcave.managers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Deprecated
public class JsonManagerV1 {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static String writeValueAsString(Object value) {
        try {
            return jsonMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readValue(String dataJson, Class<T> dataType) {
        try {
            return jsonMapper.readValue(dataJson, dataType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
