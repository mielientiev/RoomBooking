package com.roombooking.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Time;

public class JsonTimeSerializer extends JsonSerializer<Time> {

    @Override
    public void serialize(Time value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.toString().substring(0, 5));
    }
}
