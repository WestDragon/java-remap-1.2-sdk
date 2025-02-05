package ru.moysklad.remap_1_2.utils.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.entities.agents.Agent;
import ru.moysklad.remap_1_2.entities.agents.Counterparty;
import ru.moysklad.remap_1_2.entities.agents.Employee;
import ru.moysklad.remap_1_2.entities.agents.Organization;

import java.io.IOException;

/**
 * Десериализатор поля <code>agent</code>. В зависимости от метаданных, возвращает экземпляр
 * одного из классов, наследующихся от Agent: Organization, Counterparty, Employee
 */
public class AgentDeserializer extends JsonDeserializer<Agent> {
    @Override
    public Agent deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        MetaEntity me = codec.treeToValue(node, MetaEntity.class);

        if (me.getMeta() == null) throw new JsonParseException(p, "Can't parse field 'agent': meta is null");
        if (me.getMeta().getType() == null) throw new JsonParseException(p, "Can't parse field 'agent': meta.type is null");

        switch (me.getMeta().getType()) {
            case ORGANIZATION:
                return codec.treeToValue(node, Organization.class);

            case COUNTERPARTY:
                return codec.treeToValue(node, Counterparty.class);

            case EMPLOYEE:
                return codec.treeToValue(node, Employee.class);

            default:
                throw new JsonParseException(p, "Can't parse field 'agent': meta.type must be one of [organization, counterparty, employee]");
        }
    }
}
