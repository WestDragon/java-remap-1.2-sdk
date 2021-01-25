package com.lognex.api.entities.documents;

import com.lognex.api.clients.EntityClientBase;
import com.lognex.api.entities.Attribute;
import com.lognex.api.entities.EntityGetUpdateDeleteTest;
import com.lognex.api.entities.MetaEntity;
import com.lognex.api.entities.Store;
import com.lognex.api.entities.agents.Organization;
import com.lognex.api.entities.products.Product;
import com.lognex.api.responses.ListEntity;
import com.lognex.api.responses.metadata.MetadataAttributeSharedStatesResponse;
import com.lognex.api.utils.ApiClientException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static com.lognex.api.utils.params.ExpandParam.expand;
import static com.lognex.api.utils.params.FilterParam.filterEq;
import static com.lognex.api.utils.params.LimitParam.limit;
import static org.junit.Assert.*;

public class ProcessingTest extends EntityGetUpdateDeleteTest {
    @Test
    public void createTest() throws IOException, ApiClientException {
        Processing processing = new Processing();
        processing.setName("processing_" + randomString(3) + "_" + new Date().getTime());
        processing.setMoment(LocalDateTime.now());

        ListEntity<Organization> orgList = api.entity().organization().get();
        assertNotEquals(0, orgList.getRows().size());
        processing.setOrganization(orgList.getRows().get(0));

        processing.setMaterials(new ListEntity<>());
        processing.getMaterials().setRows(new ArrayList<>());
        Product material = simpleEntityManager.createSimple(Product.class);
        DocumentPosition materialPosition = new DocumentPosition();
        materialPosition.setAssortment(material);
        materialPosition.setQuantity(3.1234);
        processing.getMaterials().getRows().add(materialPosition);

        processing.setProducts(new ListEntity<>());
        processing.getProducts().setRows(new ArrayList<>());
        Product product = simpleEntityManager.createSimple(Product.class);
        DocumentPosition productPosition = new DocumentPosition();
        productPosition.setAssortment(product);
        productPosition.setQuantity(3.1234);
        processing.getProducts().getRows().add(productPosition);

        processing.setProcessingSum(randomLong(10, 10000));
        processing.setQuantity(randomDouble(1, 5, 10));

        ListEntity<Store> store = api.entity().store().get(filterEq("name", "Основной склад"));
        assertEquals(1, store.getRows().size());
        processing.setMaterialsStore(store.getRows().get(0));
        processing.setProductsStore(store.getRows().get(0));

        ProcessingPlan processingPlan = new ProcessingPlan();
        processingPlan.setName("processingplan_" + randomString(3) + "_" + new Date().getTime());

        processingPlan.setMaterials(new ListEntity<>());
        processingPlan.getMaterials().setRows(new ArrayList<>());
        ProcessingPlan.PlanItem materialItem = new ProcessingPlan.PlanItem();
        materialItem.setProduct(material);
        materialItem.setQuantity(randomDouble(1, 5, 10));
        processingPlan.getMaterials().getRows().add(materialItem);

        processingPlan.setProducts(new ListEntity<>());
        processingPlan.getProducts().setRows(new ArrayList<>());
        ProcessingPlan.PlanItem productItem = new ProcessingPlan.PlanItem();
        productItem.setProduct(product);
        productItem.setQuantity(randomDouble(1, 5, 10));
        processingPlan.getProducts().getRows().add(productItem);

        api.entity().processingplan().create(processingPlan);
        processing.setProcessingPlan(processingPlan);

        api.entity().processing().create(processing);

        ListEntity<Processing> updatedEntitiesList = api.entity().processing().
                get(limit(50), filterEq("name", processing.getName()), expand("materials"), expand("products"));
        assertEquals(1, updatedEntitiesList.getRows().size());

        Processing retrievedEntity = updatedEntitiesList.getRows().get(0);
        assertEquals(processing.getName(), retrievedEntity.getName());
        assertEquals(processing.getMoment(), retrievedEntity.getMoment());
        assertEquals(processing.getOrganization().getMeta().getHref(), retrievedEntity.getOrganization().getMeta().getHref());
        assertEquals(materialPosition.getQuantity(), retrievedEntity.getMaterials().getRows().get(0).getQuantity());
        Product retrievedMaterial = (Product) retrievedEntity.getMaterials().getRows().get(0).getAssortment();
        assertEquals(material.getMeta().getHref(), retrievedMaterial.getMeta().getHref());
        assertEquals(productPosition.getQuantity(), retrievedEntity.getProducts().getRows().get(0).getQuantity());
        Product retrievedProduct = (Product) retrievedEntity.getProducts().getRows().get(0).getAssortment();
        assertEquals(product.getMeta().getHref(), retrievedProduct.getMeta().getHref());
        assertEquals(processing.getProcessingPlan().getMeta().getHref(), retrievedEntity.getProcessingPlan().getMeta().getHref());
    }

    @Test
    public void metadataTest() throws IOException, ApiClientException {
        MetadataAttributeSharedStatesResponse response = api.entity().processing().metadata().get();

        assertFalse(response.getCreateShared());
    }

    @Test
    public void attributesTest() throws IOException, ApiClientException{
        ListEntity<Attribute> attributes = api.entity().processing().metadataAttributes();
        assertNotNull(attributes);
    }

    @Ignore
    @Override
    public void massCreateDeleteTest() {
    }

    @Override
    protected void getAsserts(MetaEntity originalEntity, MetaEntity retrievedEntity) {
        Processing originalProcessing = (Processing) originalEntity;
        Processing retrievedProcessing = (Processing) retrievedEntity;

        assertEquals(originalProcessing.getName(), retrievedProcessing.getName());
        assertEquals(originalProcessing.getOrganization().getMeta().getHref(), retrievedProcessing.getOrganization().getMeta().getHref());
        assertEquals(originalProcessing.getMaterials().getMeta().getSize(), retrievedProcessing.getMaterials().getMeta().getSize());
        assertEquals(originalProcessing.getProducts().getMeta().getSize(), retrievedProcessing.getProducts().getMeta().getSize());
        assertEquals(originalProcessing.getProcessingPlan().getMeta().getHref(), retrievedProcessing.getProcessingPlan().getMeta().getHref());
    }

    @Override
    protected void putAsserts(MetaEntity originalEntity, MetaEntity updatedEntity, Object changedField) {
        Processing originalProcessing = (Processing) originalEntity;
        Processing updatedProcessing = (Processing) updatedEntity;

        assertNotEquals(originalProcessing.getName(), updatedProcessing.getName());
        assertEquals(changedField, updatedProcessing.getName());
        assertEquals(originalProcessing.getOrganization().getMeta().getHref(), updatedProcessing.getOrganization().getMeta().getHref());
        assertEquals(originalProcessing.getMaterials().getMeta().getSize(), updatedProcessing.getMaterials().getMeta().getSize());
        assertEquals(originalProcessing.getProducts().getMeta().getSize(), updatedProcessing.getProducts().getMeta().getSize());
        assertEquals(originalProcessing.getProcessingPlan().getMeta().getHref(), updatedProcessing.getProcessingPlan().getMeta().getHref());
    }

    @Override
    protected EntityClientBase entityClient() {
        return api.entity().processing();
    }

    @Override
    protected Class<? extends MetaEntity> entityClass() {
        return Processing.class;
    }
}
