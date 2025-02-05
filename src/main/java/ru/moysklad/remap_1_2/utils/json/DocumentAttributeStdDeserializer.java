package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.moysklad.remap_1_2.entities.*;
import ru.moysklad.remap_1_2.entities.agents.Agent;
import ru.moysklad.remap_1_2.entities.products.markers.ProductAttributeMarker;
import ru.moysklad.remap_1_2.utils.MetaHrefUtils;

import java.io.IOException;
import java.time.LocalDateTime;

import static ru.moysklad.remap_1_2.entities.Attribute.Type.dictionary;

public class DocumentAttributeStdDeserializer extends StdDeserializer<DocumentAttribute> implements ResolvableDeserializer {
    private final JsonDeserializer<?> defaultDeserializer;

    public DocumentAttributeStdDeserializer(JsonDeserializer<?> defaultDeserializer) {
        super(DocumentAttribute.class);
        this.defaultDeserializer = defaultDeserializer;
    }

    @Override
    public DocumentAttribute deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        if (!node.has("type"))
            throw new IllegalArgumentException("В пришедшей сущности дополнительного параметра нет поля 'type'!");

        try {
            Meta.Type t = Meta.Type.find(node.get("type").asText());
            ((ObjectNode) node).put("type", dictionary.name());
            ((ObjectNode) node).put("entityType", t.getApiName());
        } catch (IllegalArgumentException ignored) {
        }

        DocumentAttribute attribute = null;
        try (JsonParser traverse = node.traverse(codec)) {
            traverse.nextToken();
            attribute = (DocumentAttribute) defaultDeserializer.deserialize(traverse, ctxt);
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