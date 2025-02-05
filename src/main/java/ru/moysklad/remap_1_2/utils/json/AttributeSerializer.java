package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import ru.moysklad.remap_1_2.entities.Attribute;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class AttributeSerializer extends JsonSerializer<Attribute> {

    @Override
    public void serialize(Attribute src, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType javaType = typeFactory.constructType(Attribute.class);
        BeanSerializerFactory.instance.createSerializer(serializers, javaType).serialize(src, gen, serializers);

        ObjectMapper mapper = (ObjectMapper) gen.getCodec();

        if (src.getType() != null) {
            switch (src.getType()) {
                case timeValue:
                    if (src.getValue() != null) {
                        if (src.getValue() instanceof LocalDateTime || src.getValue() instanceof LocalDate) {
                            serializers.setAttribute("value", mapper.writeValueAsString(src.getValue()));
                        } else {
                            throw new IllegalArgumentException("Unsupported type for 'time' field: " + src.getValue().getClass().getSimpleName());
                        }
                    }
                    break;
            }

        }
    }
}
