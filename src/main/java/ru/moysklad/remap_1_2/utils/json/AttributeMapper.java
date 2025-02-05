package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import ru.moysklad.remap_1_2.entities.*;
import ru.moysklad.remap_1_2.entities.agents.Agent;
import ru.moysklad.remap_1_2.entities.products.markers.ProductAttributeMarker;
import ru.moysklad.remap_1_2.utils.MetaHrefUtils;

import java.time.LocalDateTime;

import static ru.moysklad.remap_1_2.entities.Attribute.Type.dictionary;

public class AttributeMapper {

    public static Object getValue(Attribute attribute, ObjectCodec codec, JsonNode valueNode) throws JsonProcessingException {
        if(attribute.getValue() != null){
            if (attribute.getType() != null && !dictionary.equals(attribute.getType())) {
                switch (attribute.getType()) {
                    case longValue:
                        return ((Integer)attribute.getValue()).longValue();
                    case timeValue:
                        return codec.treeToValue(valueNode, LocalDateTime.class);
                }
            } else if (attribute.getEntityType() != null) {
                switch (attribute.getEntityType()) {
                    case COUNTERPARTY:
                    case ORGANIZATION:
                    case EMPLOYEE:
                        return codec.treeToValue(valueNode, Agent.class);
                    case PRODUCT:
                    case BUNDLE:
                    case SERVICE:
                        return codec.treeToValue(valueNode, ProductAttributeMarker.class);
                    case CONTRACT:
                        return codec.treeToValue(valueNode, Contract.class);
                    case PROJECT:
                        return codec.treeToValue(valueNode, Project.class);
                    case STORE:
                        return codec.treeToValue(valueNode, Store.class);
                    case CUSTOM_ENTITY:
                        CustomEntityElement customEntity = codec.treeToValue(valueNode, CustomEntityElement.class);
                        if (customEntity != null) {
                            customEntity.setCustomDictionaryId(MetaHrefUtils.getCustomDictionaryIdFromHref(customEntity.getMeta().getHref()));
                        }
                        return customEntity;
                }
            }
        }

        return null;
    }
}
