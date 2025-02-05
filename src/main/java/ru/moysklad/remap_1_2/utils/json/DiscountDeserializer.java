package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.entities.discounts.*;

import java.io.IOException;

/**
 * Десериализатор поля <code>discount</code>. В зависимости от метаданных, возвращает экземпляр
 * одного из классов, наследующихся от Discount: AccumulationDiscount,
 * BonusProgram, PersonalDiscount, SpecialPriceDiscount, RoundOffDiscount
 * или сам Discount
 */
public class DiscountDeserializer extends JsonDeserializer<Discount> {

    @Override
    public Discount deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        MetaEntity me = codec.treeToValue(node, MetaEntity.class);

        if (me.getMeta() == null) throw new JsonParseException(p, "Can't parse field 'discount': meta is null");
        if (me.getMeta().getType() == null)
            throw new JsonParseException(p, "Can't parse field 'discount': meta.type is null");

        switch (me.getMeta().getType()) {
            case ACCUMULATION_DISCOUNT:
                return codec.treeToValue(node, AccumulationDiscount.class);

            case BONUS_PROGRAM:
                return codec.treeToValue(node, BonusProgram.class);

            case DISCOUNT:
                return codec.treeToValue(node, Discount.class);

            case PERSONAL_DISCOUNT:
                return codec.treeToValue(node, PersonalDiscount.class);

            case SPECIAL_PRICE_DISCOUNT:
                return codec.treeToValue(node, SpecialPriceDiscount.class);

            case ROUND_OFF_DISCOUNT:
                return codec.treeToValue(node, RoundOffDiscount.class);

            default:
                throw new JsonParseException(p, "Can't parse field 'discount': meta.type must be one of [accumulationdiscount, bonusprogram, discount, personaldiscount, specialpricediscount, roundoffdiscount]");
        }
    }
}
