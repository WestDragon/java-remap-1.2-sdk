package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.moysklad.remap_1_2.entities.Assortment;
import ru.moysklad.remap_1_2.entities.Consignment;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.entities.products.Bundle;
import ru.moysklad.remap_1_2.entities.products.Product;
import ru.moysklad.remap_1_2.entities.products.Service;
import ru.moysklad.remap_1_2.entities.products.Variant;

import java.io.IOException;

/**
 * Cериализатор классов-наследников интерфейса <code>Assortment</code>. В зависимости от метаданных,
 * возвращает экземпляр одного из классов: Product, Service, Bundle, Variant
 */
public class AssortmentDeserializer extends JsonDeserializer<Assortment> {
    @Override
    public Assortment deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        MetaEntity me = codec.treeToValue(node, MetaEntity.class);

        if (me.getMeta() == null) throw new JsonParseException(p, "Can't parse field 'product': meta is null");
        if (me.getMeta().getType() == null)
            throw new JsonParseException(p, "Can't parse field 'product': meta.type is null");

        switch (me.getMeta().getType()) {
            case PRODUCT:
                return codec.treeToValue(node, Product.class);

            case SERVICE:
                return codec.treeToValue(node, Service.class);

            case BUNDLE:
                return codec.treeToValue(node, Bundle.class);

            case VARIANT:
                return codec.treeToValue(node, Variant.class);

            case CONSIGNMENT:
                return codec.treeToValue(node, Consignment.class);

            default:
                throw new JsonParseException(p, "Can't parse field 'product': meta.type must be one of [product, service, bundle, variant, consignment]");
        }
    }
}
