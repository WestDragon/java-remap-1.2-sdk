package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.moysklad.remap_1_2.entities.*;

import java.io.IOException;

import static ru.moysklad.remap_1_2.entities.Attribute.Type.dictionary;

public class AttributeStdDeserializer extends StdDeserializer<Attribute> implements ResolvableDeserializer {
    private final JsonDeserializer<?> defaultDeserializer;

    public AttributeStdDeserializer(JsonDeserializer<?> defaultDeserializer) {
        super(Attribute.class);
        this.defaultDeserializer = defaultDeserializer;
    }

    @Override
    public Attribute deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        if (!node.has("type")) {
            throw new IllegalArgumentException("В пришедшей сущности дополнительного параметра нет поля 'type'!");
        }


        try {
            Meta.Type t = Meta.Type.find(node.get("type").asText());
            ((ObjectNode) node).put("type", dictionary.name());
            ((ObjectNode) node).put("entityType", t.getApiName());
        } catch (IllegalArgumentException ignored) {
        }

        Attribute attribute = null;
        try (JsonParser traverse = node.traverse(codec)) {
            traverse.nextToken();
            attribute = (Attribute) defaultDeserializer.deserialize(traverse, ctxt);
        }

        Object value = AttributeMapper.getValue(attribute, codec, node.get("value"));

        if(value != null){
            attribute.setValue(value);
        }

        return attribute;
    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
    }

}