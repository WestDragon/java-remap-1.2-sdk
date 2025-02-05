package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.entities.documents.DocumentEntity;

import java.io.IOException;

public class DocumentEntityDeserializer extends JsonDeserializer<DocumentEntity> {
    @Override
    public DocumentEntity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        MetaEntity me = codec.treeToValue(node, MetaEntity.class);

        if (me.getMeta() == null) {
            throw new JsonParseException(p, "Can't parse field 'operation': meta is null");
        } else if (me.getMeta().getType() == null) {
            throw new JsonParseException(p, "Can't parse field 'operation': meta.type is null");
        }

        if (!DocumentEntity.class.isAssignableFrom(me.getMeta().getType().getModelClass())) {
            throw new JsonParseException(p, "Can't parse field 'operation': meta.type is not a valid document type");
        }

        return (DocumentEntity) codec.treeToValue(node, me.getMeta().getType().getModelClass());
    }
}
