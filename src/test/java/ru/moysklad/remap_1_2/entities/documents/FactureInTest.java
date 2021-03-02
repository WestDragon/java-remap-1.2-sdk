package ru.moysklad.remap_1_2.entities.documents;

import org.junit.Test;
import ru.moysklad.remap_1_2.clients.EntityClientBase;
import ru.moysklad.remap_1_2.entities.Attribute;
import ru.moysklad.remap_1_2.entities.EntityGetUpdateDeleteTest;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.responses.ListEntity;
import ru.moysklad.remap_1_2.responses.metadata.MetadataAttributeSharedStatesResponse;
import ru.moysklad.remap_1_2.utils.ApiClientException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static ru.moysklad.remap_1_2.utils.params.FilterParam.filterEq;
import static org.junit.Assert.*;

public class FactureInTest extends EntityGetUpdateDeleteTest {
    @Test
    public void createTest() throws IOException, ApiClientException {
        FactureIn factureIn = new FactureIn();
        factureIn.setName("facturein_" + randomString(3) + "_" + new Date().getTime());
        factureIn.setDescription(randomString());
        factureIn.setMoment(LocalDateTime.now());
        factureIn.setIncomingNumber(randomString());
        factureIn.setIncomingDate(LocalDateTime.now());
        List<Supply> supplies = new ArrayList<>();
        supplies.add(simpleEntityManager.createSimpleSupply());
        factureIn.setSupplies(supplies);

        api.entity().facturein().create(factureIn);

        ListEntity<FactureIn> updatedEntitiesList = api.entity().facturein().get(filterEq("name", factureIn.getName()));
        assertEquals(1, updatedEntitiesList.getRows().size());

        FactureIn retrievedEntity = updatedEntitiesList.getRows().get(0);
        assertEquals(factureIn.getName(), retrievedEntity.getName());
        assertEquals(factureIn.getDescription(), retrievedEntity.getDescription());
        assertEquals(factureIn.getMoment(), retrievedEntity.getMoment());
        assertEquals(factureIn.getIncomingNumber(), retrievedEntity.getIncomingNumber());
        assertEquals(factureIn.getIncomingDate(), retrievedEntity.getIncomingDate());
        assertEquals(factureIn.getOrganization().getMeta().getHref(), retrievedEntity.getOrganization().getMeta().getHref());
        assertEquals(factureIn.getSupplies().get(0).getMeta().getHref(), retrievedEntity.getSupplies().get(0).getMeta().getHref());
    }

    @Test
    public void metadataTest() throws IOException, ApiClientException {
        MetadataAttributeSharedStatesResponse response = api.entity().facturein().metadata().get();

        assertFalse(response.getCreateShared());
    }

    @Test
    public void attributesTest() throws IOException, ApiClientException{
        ListEntity<Attribute> attributes = api.entity().facturein().metadataAttributes();
        assertNotNull(attributes);
    }

    @Test
    public void newBySuppliesTest() throws IOException, ApiClientException {
        Supply supply = simpleEntityManager.createSimple(Supply.class);

        FactureIn factureIn = api.entity().facturein().templateDocument("supplies", Collections.singletonList(supply));
        LocalDateTime time = LocalDateTime.now();

        assertEquals("", factureIn.getName());
        assertEquals(supply.getSum(), factureIn.getSum());
        assertFalse(factureIn.getShared());
        assertTrue(factureIn.getApplicable());
        assertFalse(factureIn.getPublished());
        assertFalse(factureIn.getPrinted());
        assertTrue(ChronoUnit.MILLIS.between(time, factureIn.getMoment()) < 1000);
        assertEquals(1, factureIn.getSupplies().size());
        assertEquals(supply.getMeta().getHref(), factureIn.getSupplies().get(0).getMeta().getHref());
        assertEquals(supply.getGroup().getMeta().getHref(), factureIn.getGroup().getMeta().getHref());
        assertEquals(supply.getAgent().getMeta().getHref(), factureIn.getAgent().getMeta().getHref());
        assertEquals(supply.getOrganization().getMeta().getHref(), factureIn.getOrganization().getMeta().getHref());
    }

    @Test
    public void newByPaymentsOutTest() throws IOException, ApiClientException {
        PaymentOut paymentOut = simpleEntityManager.createSimple(PaymentOut.class);

        FactureIn factureIn = api.entity().facturein().templateDocument("payments", Collections.singletonList(paymentOut));
        LocalDateTime time = LocalDateTime.now();

        assertEquals("", factureIn.getName());
        assertEquals(paymentOut.getSum(), factureIn.getSum());
        assertFalse(factureIn.getShared());
        assertTrue(factureIn.getApplicable());
        assertTrue(ChronoUnit.MILLIS.between(time, factureIn.getMoment()) < 1000);
        assertEquals(1, factureIn.getPayments().size());
        assertEquals(paymentOut.getMeta().getHref(), ((PaymentOut) factureIn.getPayments().get(0)).getMeta().getHref());
        assertEquals(paymentOut.getGroup().getMeta().getHref(), factureIn.getGroup().getMeta().getHref());
        assertEquals(paymentOut.getAgent().getMeta().getHref(), factureIn.getAgent().getMeta().getHref());
        assertEquals(paymentOut.getOrganization().getMeta().getHref(), factureIn.getOrganization().getMeta().getHref());
    }

    @Override
    protected void getAsserts(MetaEntity originalEntity, MetaEntity retrievedEntity) {
        FactureIn originalFactureIn = (FactureIn) originalEntity;
        FactureIn retrievedFactureIn = (FactureIn) retrievedEntity;

        assertEquals(originalFactureIn.getName(), retrievedFactureIn.getName());
        assertEquals(originalFactureIn.getIncomingNumber(), retrievedFactureIn.getIncomingNumber());
        assertEquals(originalFactureIn.getIncomingDate(), retrievedFactureIn.getIncomingDate());
        assertEquals(originalFactureIn.getOrganization().getMeta().getHref(), retrievedFactureIn.getOrganization().getMeta().getHref());
        assertEquals(originalFactureIn.getSupplies().get(0).getMeta().getHref(), retrievedFactureIn.getSupplies().get(0).getMeta().getHref());
    }

    @Override
    protected void putAsserts(MetaEntity originalEntity, MetaEntity updatedEntity, Object changedField) {
        FactureIn originalFactureIn = (FactureIn) originalEntity;
        FactureIn updatedFactureIn = (FactureIn) updatedEntity;

        assertNotEquals(originalFactureIn.getName(), updatedFactureIn.getName());
        assertEquals(changedField, updatedFactureIn.getName());
        assertEquals(originalFactureIn.getIncomingNumber(), updatedFactureIn.getIncomingNumber());
        assertEquals(originalFactureIn.getIncomingDate(), updatedFactureIn.getIncomingDate());
        assertEquals(originalFactureIn.getOrganization().getMeta().getHref(), updatedFactureIn.getOrganization().getMeta().getHref());
        assertEquals(originalFactureIn.getSupplies().get(0).getMeta().getHref(), updatedFactureIn.getSupplies().get(0).getMeta().getHref());
    }

    @Override
    protected EntityClientBase entityClient() {
        return api.entity().facturein();
    }

    @Override
    protected Class<? extends MetaEntity> entityClass() {
        return FactureIn.class;
    }
}
