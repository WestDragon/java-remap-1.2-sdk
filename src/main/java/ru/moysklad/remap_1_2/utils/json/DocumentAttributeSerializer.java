package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;
import ru.moysklad.remap_1_2.entities.Attribute;
import ru.moysklad.remap_1_2.entities.DocumentAttribute;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DocumentAttributeSerializer extends JsonSerializer<DocumentAttribute> {

    @Override
    public void serialize(DocumentAttribute src, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType javaType = typeFactory.constructType(DocumentAttribute.class);
        BeanSerializerFactory.instance.createSerializer(serializers, javaType).serialize(src, gen, serializers);

        ObjectMapper mapper = (ObjectMapper) gen.getCodec();

        if (src.getType() != null) {
            switch (src.getType()) {
                case timeValue:
                    if (src.getValue() != null) {
                        if (src.getValue() instanceof LocalDateTime || src.getValue() instanceof LocalDate) {
                            src.setValue(mapper.writeValueAsString(src.getValue()));
                        } else {
                            throw new IllegalArgumentException("Неподдерживаемый тип данных для дополнительного поля с типом 'time': " + src
                                    .getValue().getClass().getSimpleName());
                        }
                    }
                    break;
            }
        }
    }
}
