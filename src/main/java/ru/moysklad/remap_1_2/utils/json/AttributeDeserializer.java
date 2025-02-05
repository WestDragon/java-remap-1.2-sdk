package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.moysklad.remap_1_2.entities.*;
import ru.moysklad.remap_1_2.entities.agents.Agent;
import ru.moysklad.remap_1_2.entities.products.markers.ProductAttributeMarker;
import ru.moysklad.remap_1_2.utils.MetaHrefUtils;

import java.io.IOException;
import java.time.LocalDateTime;

public class AttributeDeserializer extends JsonDeserializer<Attribute> {
    @Override
    public Attribute deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        if (!node.has("type"))
            throw new IllegalArgumentException("В пришедшей сущности дополнительного параметра нет поля 'type'!");

        String attrType = node.get("type").asText();

        try {
            Meta.Type t = Meta.Type.find(attrType);
            ((ObjectNode) node).remove("type");
            ((ObjectNode) node).put("entityType", t.getApiName());
        } catch (IllegalArgumentException ignored) {
        }

        Attribute attribute = codec.treeToValue(node, Attribute.class);

        if (attribute.getType() != null && attribute.getValue() != null) {
            switch (attribute.getType()) {
                case longValue:
                    attribute.setValue(((Integer)attribute.getValue()).longValue());
                    break;

                case timeValue:
                    attribute.setValue(codec.treeToValue(node.get("value"), LocalDateTime.class));
                    break;
            }
        } else if (attribute.getEntityType() != null) {
            switch (attribute.getEntityType()) {
                case COUNTERPARTY:
                case ORGANIZATION:
                case EMPLOYEE:
                    attribute.setValue(
                            codec.treeToValue(node.get("value"), Agent.class)
                    );
                    break;

                case PRODUCT:
                case BUNDLE:
                case SERVICE:
                    attribute.setValue(
                            codec.treeToValue(node.get("value"), ProductAttributeMarker.class)
                    );
                    break;

                case CONTRACT:
                    attribute.setValue(
                            codec.treeToValue(node.get("value"), Contract.class)
                    );
                    break;

                case PROJECT:
                    attribute.setValue(
                            codec.treeToValue(node.get("value"), Project.class)
                    );
                    break;

                case STORE:
                    attribute.setValue(
                            codec.treeToValue(node.get("value"), Store.class)
                    );
                    break;

                case CUSTOM_ENTITY:
                    CustomEntityElement customEntity = codec.treeToValue(node.get("value"), CustomEntityElement.class);
                    if (customEntity != null) {
                        customEntity.setCustomDictionaryId(MetaHrefUtils.getCustomDictionaryIdFromHref(customEntity.getMeta().getHref()));
                    }
                    attribute.setValue(customEntity);
                    break;
            }
        }

        return attribute;
    }
}
