package com.myadridev.mypocketcave.models;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

public class CoordinatesModelDeserializer extends StdKeyDeserializer {

    public CoordinatesModelDeserializer() {
        super(TYPE_CLASS, CoordinatesModel.class);
    }

    @Override
    public CoordinatesModel _parse(String key, DeserializationContext context) throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(key, CoordinatesModel.class);
        } catch (Exception ex) {
            throw context.weirdKeyException(_keyClass, key, ex.getMessage());
        }
    }
}