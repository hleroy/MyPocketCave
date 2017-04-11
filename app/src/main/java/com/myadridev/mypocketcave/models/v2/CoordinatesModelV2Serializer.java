package com.myadridev.mypocketcave.models.v2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CoordinatesModelV2Serializer extends StdSerializer<CoordinatesModelV2> {

    public CoordinatesModelV2Serializer() {
        super(CoordinatesModelV2.class);
    }

    @Override
    public void serialize(CoordinatesModelV2 value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        jsonGenerator.writeFieldName(mapper.writeValueAsString(value));
    }
}