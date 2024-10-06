package ru.moysklad.remap_1_2.utils.params;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FieldsParam extends ApiParam {
    private final String[] fields;

    private FieldsParam(String[] fields) {
        super(Type.fields);
        this.fields = fields;
    }

    public static FieldsParam fields(String... fields) {
        if (fields == null || fields.length == 0) return null;
        return new FieldsParam(fields);
    }

    @Override
    protected String render(String host) {
        return Arrays.stream(fields).collect(Collectors.joining(type.getSeparator()));
    }
}
