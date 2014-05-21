package com.roombooking.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Time;

public class JsonTimeDeserializer extends JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException {
        try {
            return Time.valueOf(jsonparser.getText() + ":00");
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}