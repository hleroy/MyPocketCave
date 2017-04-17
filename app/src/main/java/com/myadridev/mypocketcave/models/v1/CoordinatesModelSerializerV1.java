package com.myadridev.mypocketcave.models.v1;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

@Deprecated
public class CoordinatesModelSerializerV1 extends StdSerializer<CoordinatesModelV1> {

    public CoordinatesModelSerializerV1() {
        super(CoordinatesModelV1.class);
    }

    @Override
    public void serialize(CoordinatesModelV1 value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        jsonGenerator.writeFieldName(mapper.writeValueAsString(value));
    }
}