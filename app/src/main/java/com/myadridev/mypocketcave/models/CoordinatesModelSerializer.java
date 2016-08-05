package com.myadridev.mypocketcave.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CoordinatesModelSerializer extends StdSerializer<CoordinatesModel> {

    public CoordinatesModelSerializer() {
        super(CoordinatesModel.class);
    }

    @Override
    public void serialize(CoordinatesModel value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        jsonGenerator.writeFieldName(mapper.writeValueAsString(value));
    }
}