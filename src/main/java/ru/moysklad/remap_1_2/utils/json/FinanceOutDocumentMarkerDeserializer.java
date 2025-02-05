package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.entities.documents.CashOut;
import ru.moysklad.remap_1_2.entities.documents.PaymentOut;
import ru.moysklad.remap_1_2.entities.documents.markers.FinanceOutDocumentMarker;

import java.io.IOException;

/**
 * Cериализатор классов-наследников интерфейса <code>FinanceOutDocumentMarker</code>. В зависимости от метаданных,
 * возвращает экземпляр одного из классов: CashIn, CashOut, PaymentIn,
 * PaymentOut
 */
public class FinanceOutDocumentMarkerDeserializer extends JsonDeserializer<FinanceOutDocumentMarker> {
    @Override
    public FinanceOutDocumentMarker deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        MetaEntity me = codec.treeToValue(node, MetaEntity.class);

        if (me.getMeta() == null) {
            throw new JsonParseException(p, "Can't parse field 'payments': meta is null");
        }
        if (me.getMeta().getType() == null) {
            throw new JsonParseException(p, "Can't parse field 'payments': meta.type is null");
        }
        switch (me.getMeta().getType()) {
            case CASH_OUT:
                return codec.treeToValue(node, CashOut.class);

            case PAYMENT_OUT:
                return codec.treeToValue(node, PaymentOut.class);

            default:
                throw new JsonParseException(p, "Can't parse field 'payments': meta.type must be one of [cashin, cashout, paymentin, paymentout]");
        }
    }
}
