package ru.moysklad.remap_1_2.entities;

import ru.moysklad.remap_1_2.clients.EntityClientBase;
import ru.moysklad.remap_1_2.entities.agents.Counterparty;
import ru.moysklad.remap_1_2.responses.ListEntity;
import ru.moysklad.remap_1_2.utils.ApiClientException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import static ru.moysklad.remap_1_2.utils.params.FilterParam.filterEq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BonusTransactionTest  extends EntityGetUpdateDeleteTest {
    @Test
    @Ignore
    public void createTest() throws IOException, ApiClientException {
        BonusTransaction bonusTransaction = new BonusTransaction();
        bonusTransaction.setName("bonusTransaction_" + randomString(3) + "_" + new Date().getTime());
        Counterparty agent = simpleEntityManager.createSimpleCounterparty();
        bonusTransaction.setAgent(agent);
        bonusTransaction.setBonusProgram(null); // can not create bonusprogram through api
        bonusTransaction.setExternalCode(randomString(5));

        api.entity().bonustransaction().create(bonusTransaction);

        ListEntity<BonusTransaction> updatedEntitiesList = api.entity().bonustransaction().get(filterEq("name", bonusTransaction.getName()));
        assertEquals(1, updatedEntitiesList.getRows().size());

        BonusTransaction retrievedEntity = updatedEntitiesList.getRows().get(0);
        assertEquals(bonusTransaction.getName(), retrievedEntity.getName());
        assertEquals(bonusTransaction.getExternalCode(), retrievedEntity.getExternalCode());
    }

    @Override
    protected void getAsserts(MetaEntity originalEntity, MetaEntity retrievedEntity) {
        BonusTransaction originalBonusTransaction = (BonusTransaction) originalEntity;
        BonusTransaction retrievedBonusTransaction = (BonusTransaction) retrievedEntity;

        assertEquals(originalBonusTransaction.getName(), retrievedBonusTransaction.getName());
    }

    @Override
    protected void putAsserts(MetaEntity originalEntity, MetaEntity updatedEntity, Object changedField) {
        BonusTransaction originalBonusTransaction = (BonusTransaction) originalEntity;
        BonusTransaction updatedBonusTransaction = (BonusTransaction) updatedEntity;

        assertNotEquals(originalBonusTransaction.getName(), updatedBonusTransaction.getName());
        assertEquals(changedField, updatedBonusTransaction.getName());
    }

    @Ignore
    @Test
    @Override
    public void putTest() throws IOException, ApiClientException {
    }

    @Ignore
    @Test
    @Override
    public void deleteTest() throws IOException, ApiClientException {
    }

    @Ignore
    @Test
    @Override
    public void getTest() throws IOException, ApiClientException {
    }

    @Ignore
    @Test
    @Override
    public void massUpdateTest() {
    }

    @Ignore
    @Test
    @Override
    public void massCreateDeleteTest() {
    }

    @Override
    protected EntityClientBase entityClient() {
        return api.entity().bonustransaction();
    }

    @Override
    protected Class<? extends MetaEntity> entityClass() {
        return BonusTransaction.class;
    }
}
