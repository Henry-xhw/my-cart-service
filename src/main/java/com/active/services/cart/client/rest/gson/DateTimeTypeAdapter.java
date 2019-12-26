package com.active.services.cart.client.rest.gson;

import com.active.services.domain.DateTime;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Custom DateTime adapter for use with GSON
 */
public class DateTimeTypeAdapter extends TypeAdapter<DateTime> {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Override
    public void write(JsonWriter out, DateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(value.toDate().toInstant(), ZoneOffset.UTC);
            out.value(dateTimeFormatter.format(localDateTime));
        }
    }

    @Override
    public DateTime read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        String dateAsString = reader.nextString();
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateAsString, dateTimeFormatter);
            return new DateTime(Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant()));
        } catch (Exception e) {
            throw new DateTimeParseException(dateAsString);
        }
    }

    private class DateTimeParseException extends RuntimeException  {
        public DateTimeParseException(String value) {
            super("Failed to parse DateTime: " + value);
        }
    }
}
