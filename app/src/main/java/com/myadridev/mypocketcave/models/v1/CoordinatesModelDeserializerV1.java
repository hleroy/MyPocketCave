package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;

@Deprecated
public class CoordinatesModelDeserializerV1 extends StdKeyDeserializer {

    public CoordinatesModelDeserializerV1() {
        super(TYPE_CLASS, CoordinatesModelV1.class);
    }

    @Override
    public CoordinatesModelV1 _parse(String key, DeserializationContext context) throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(key, CoordinatesModelV1.class);
        } catch (Exception ex) {
            throw context.weirdKeyException(_keyClass, key, ex.getMessage());
        }
    }
}