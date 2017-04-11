package com.myadridev.mypocketcave.models.v2;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

public class CoordinatesModelV2Deserializer extends StdKeyDeserializer {

    public CoordinatesModelV2Deserializer() {
        super(TYPE_CLASS, CoordinatesModelV2.class);
    }

    @Override
    public CoordinatesModelV2 _parse(String key, DeserializationContext context) throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(key, CoordinatesModelV2.class);
        } catch (Exception ex) {
            throw context.weirdKeyException(_keyClass, key, ex.getMessage());
        }
    }
}