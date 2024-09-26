package ru.moysklad.remap_1_2.serializers;

import com.google.gson.Gson;
import org.junit.Test;
import ru.moysklad.remap_1_2.ApiClient;
import ru.moysklad.remap_1_2.entities.*;
import ru.moysklad.remap_1_2.entities.products.Product;
import ru.moysklad.remap_1_2.utils.TestAsserts;
import ru.moysklad.remap_1_2.utils.TestRandomizers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class AttributeSerializerTest implements TestAsserts, TestRandomizers {
    @Test
    public void test_deserializeString() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation();
        e.setType(AttributeOperation.Type.stringValue);
        e.setValue("STRING");

        String data = gsonCustom.toJson(e);

        assertEquals("{\"type\":\"string\",\"value\":\"STRING\"}", gsonCustom.toJson(e));
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(AttributeOperation.Type.stringValue, parsed.getType());
        assertNull(parsed.getEntityType());
        assertEquals("STRING", parsed.getValue());
        assertEquals(String.class, parsed.getValue().getClass());
    }

    @Test
    public void test_deserializeLong() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation();
        e.setType(AttributeOperation.Type.longValue);
        e.setValue(1234567L);

        String data = gsonCustom.toJson(e);

        assertEquals("{\"type\":\"long\",\"value\":1234567}", gsonCustom.toJson(e));
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(AttributeOperation.Type.longValue, parsed.getType());
        assertNull(parsed.getEntityType());
        assertEquals(1234567L, parsed.getValue());
        assertEquals(Long.class, parsed.getValue().getClass());
    }

    @Test
    public void test_deserializeTime() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation();
        e.setType(AttributeOperation.Type.timeValue);

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        e.setValue(date);

        String data = gsonCustom.toJson(e);

        assertEquals("{\"type\":\"time\",\"value\":\"" + date.format(formatter) + "\"}", data);
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(AttributeOperation.Type.timeValue, parsed.getType());
        assertNull(parsed.getEntityType());
        assertEquals(date, parsed.getValue());
        assertEquals(LocalDateTime.class, parsed.getValue().getClass());
    }

    @Test
    public void test_deserializeFile() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation();
        e.setType(AttributeOperation.Type.fileValue);
        e.setValue("picture");
        e.setDownload(new Meta());
        e.getDownload().setHref("[URL]");
        e.getDownload().setMediaType(MediaType.octet_stream);

        String data = gsonCustom.toJson(e);

        assertEquals("{\"type\":\"file\",\"value\":\"picture\",\"download\":{\"href\":\"[URL]\",\"mediaType\":\"application/octet-stream\"}}", data);
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(AttributeOperation.Type.fileValue, parsed.getType());
        assertNull(parsed.getEntityType());
        assertEquals("picture", parsed.getValue());
        assertEquals(String.class, parsed.getValue().getClass());
        assertNotNull(parsed.getDownload());
        assertEquals("[URL]", parsed.getDownload().getHref());
        assertEquals(MediaType.octet_stream, parsed.getDownload().getMediaType());
    }

    @Test
    public void test_deserializeDouble() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation();
        e.setType(AttributeOperation.Type.doubleValue);
        e.setValue(12.345);

        String data = gsonCustom.toJson(e);

        assertEquals("{\"type\":\"double\",\"value\":12.345}", data);
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(AttributeOperation.Type.doubleValue, parsed.getType());
        assertNull(parsed.getEntityType());
        assertEquals(12.345, parsed.getValue());
        assertEquals(Double.class, parsed.getValue().getClass());
    }

    @Test
    public void test_deserializeBoolean() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation();
        e.setType(AttributeOperation.Type.booleanValue);
        e.setValue(true);

        String data = gsonCustom.toJson(e);

        assertEquals("{\"type\":\"boolean\",\"value\":true}", data);
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(AttributeOperation.Type.booleanValue, parsed.getType());
        assertNull(parsed.getEntityType());
        assertEquals(true, parsed.getValue());
        assertEquals(Boolean.class, parsed.getValue().getClass());
    }

    @Test
    public void test_deserializeText() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation();
        e.setType(AttributeOperation.Type.textValue);
        e.setValue(
                "123\n" +
                        "456\n" +
                        "789\n" +
                        "abc\n" +
                        "DEF"
        );

        String data = gsonCustom.toJson(e);

        assertEquals("{\"type\":\"text\",\"value\":\"123\\n456\\n789\\nabc\\nDEF\"}", data);
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(AttributeOperation.Type.textValue, parsed.getType());
        assertNull(parsed.getEntityType());
        assertEquals("123\n456\n789\nabc\nDEF", parsed.getValue());
        assertEquals(String.class, parsed.getValue().getClass());
    }

    @Test
    public void test_deserializeLink() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation;
        e.setType(AttributeOperation.Type.linkValue);
        e.setValue("http://moysklad.ru");

        String data = gsonCustom.toJson(e);

        assertEquals("{\"type\":\"link\",\"value\":\"http://moysklad.ru\"}", data);
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(AttributeOperation.Type.linkValue, parsed.getType());
        assertNull(parsed.getEntityType());
        assertEquals("http://moysklad.ru", parsed.getValue());
        assertEquals(String.class, parsed.getValue().getClass());
    }

    @Test
    public void test_deserializeProductEntity() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation();
        e.setEntityType(Meta.Type.PRODUCT);
        Product pr = new Product();
        pr.setMeta(new Meta());
        pr.getMeta().setType(Meta.Type.PRODUCT);
        pr.setName("PRODUCT");
        e.setValue(pr);

        String data = gsonCustom.toJson(e);

        assertEquals("{\"value\":{\"name\":\"PRODUCT\",\"meta\":{\"type\":\"product\"}},\"type\":\"product\"}", data);
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(Meta.Type.PRODUCT, parsed.getEntityType());
        assertNull(parsed.getType());
        assertEquals(Product.class, parsed.getValue().getClass());
        assertEquals("PRODUCT", parsed.getValueAs(Product.class).getName());
    }

    @Test
    public void test_deserializeProductFolderEntity() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation();
        e.setEntityType(Meta.Type.PRODUCT);
        ProductFolder prf = new ProductFolder();
        prf.setMeta(new Meta());
        prf.getMeta().setType(Meta.Type.PRODUCT_FOLDER);
        prf.setName("PRODUCT_FOLDER");
        e.setValue(prf);

        String data = gsonCustom.toJson(e);

        assertEquals("{\"value\":{\"name\":\"PRODUCT_FOLDER\",\"meta\":{\"type\":\"productfolder\"}},\"type\":\"product\"}", data);
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(Meta.Type.PRODUCT, parsed.getEntityType());
        assertNull(parsed.getType());
        assertEquals(ProductFolder.class, parsed.getValue().getClass());
        assertEquals("PRODUCT_FOLDER", parsed.getValueAs(ProductFolder.class).getName());
    }

    @Test
    public void test_deserializeCustomEntity() {
        Gson gsonCustom = ApiClient.createGson();

        Meta customEntityMeta = new Meta();
        customEntityMeta.setHref("customentity/12341234");
        customEntityMeta.setType(Meta.Type.CUSTOM_ENTITY_METADATA);

        AttributeOperation e = new AttributeOperation();
        e.setEntityType(Meta.Type.CUSTOM_ENTITY);
        e.setCustomEntityMeta(customEntityMeta);
        CustomEntityElement ce = new CustomEntityElement();
        ce.setMeta(new Meta());
        ce.getMeta().setType(Meta.Type.CUSTOM_ENTITY);
        ce.setName("CUSTOM VALUE");
        e.setValue(ce);

        String data = gsonCustom.toJson(e);

        assertEquals("{\"value\":{\"name\":\"CUSTOM VALUE\",\"meta\":{\"type\":\"customentity\"}}," +
                "\"customEntityMeta\":{\"href\":\"customentity/12341234\",\"type\":\"customentitymetadata\"}," +
                "\"type\":\"customentity\"}", data);
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(Meta.Type.CUSTOM_ENTITY, parsed.getEntityType());
        assertNull(parsed.getType());
        assertEquals(CustomEntityElement.class, parsed.getValue().getClass());
        assertEquals("CUSTOM VALUE", parsed.getValueAs(CustomEntityElement.class).getName());
        assertEquals("customentity/12341234", customEntityMeta.getHref());
        assertEquals(Meta.Type.CUSTOM_ENTITY_METADATA, customEntityMeta.getType());
    }

    @Test
    public void test_deserializeNullTime() {
        Gson gsonCustom = ApiClient.createGson();

        AttributeOperation e = new AttributeOperation();
        e.setType(AttributeOperation.Type.timeValue);

        String data = gsonCustom.toJson(e);

        assertEquals("{\"type\":\"time\"}", data);
        AttributeOperation parsed = gsonCustom.fromJson(data, AttributeOperation.class);
        assertEquals(AttributeOperation.Type.timeValue, parsed.getType());
        assertNull(parsed.getEntityType());
        assertNull(parsed.getValue());
    }
}
