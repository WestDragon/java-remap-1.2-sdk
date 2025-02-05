package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.moysklad.remap_1_2.utils.Constants.DATE_FORMAT_PATTERN;

/**
 * Created by v.hovanski on 16.07.15.
 */
public class LocalDateSerializer extends StdSerializer<LocalDate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

    public LocalDateSerializer() { super(LocalDate.class); }
    @Override
    public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeObject(value == null ? null : formatter.format(value));
    }
}
