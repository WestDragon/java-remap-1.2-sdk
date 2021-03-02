package ru.moysklad.remap_1_2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.moysklad.remap_1_2.entities.ExportExtension;
import ru.moysklad.remap_1_2.entities.Store;
import ru.moysklad.remap_1_2.entities.Template;
import ru.moysklad.remap_1_2.entities.agents.Organization;
import ru.moysklad.remap_1_2.entities.documents.Demand;
import ru.moysklad.remap_1_2.responses.ListEntity;
import ru.moysklad.remap_1_2.utils.ApiClientException;
import ru.moysklad.remap_1_2.utils.TestConstants;

import java.io.File;
import java.io.IOException;

import static ru.moysklad.remap_1_2.clients.endpoints.ExportEndpoint.PrintRequest.printRequest;
import static org.junit.Assert.*;

public class ExportTest {
    private ApiClient api;

    @Before
    public void init() {
        api = new ApiClient(
                System.getenv("API_HOST"),
                TestConstants.FORCE_HTTPS_FOR_TESTS, System.getenv("API_LOGIN"),
                System.getenv("API_PASSWORD")
        );
    }

    @After
    public void antiLimits() throws InterruptedException {
        Thread.sleep(1500); // Защита от лимитов
    }

    @Test
    public void test_exportSingle() throws IOException, ApiClientException {
        String docId;
        {
            Organization ag = api.entity().organization().get().getRows().get(0);
            Store st = api.entity().store().get().getRows().get(0);

            Demand doc = new Demand();
            doc.setAgent(ag);
            doc.setOrganization(ag);
            doc.setStore(st);
            api.entity().demand().create(doc);

            docId = doc.getId();
        }

        ListEntity<Template> templates = api.entity().demand().metadata().embeddedtemplate();
        assertNotNull(templates);
        assertNotNull(templates.getRows());
        assertFalse(templates.getRows().isEmpty());

        File exportFileXls = new File("test_single.xls");
        api.entity().demand().export(docId, templates.getRows().get(0), exportFileXls);
        assertTrue(exportFileXls.exists());
        assertTrue(exportFileXls.length() > 0);

        File exportFilePdf = new File("test_single.pdf");
        api.entity().demand().export(docId, templates.getRows().get(0), exportFilePdf);
        assertTrue(exportFilePdf.exists());
        assertTrue(exportFilePdf.length() > 0);

        File exportFileNoExt = new File("test_single_without_ext");
        api.entity().demand().export(docId, templates.getRows().get(0), ExportExtension.html, exportFileNoExt);
        assertTrue(exportFileNoExt.exists());
        assertTrue(exportFileNoExt.length() > 0);

        File exportFileDiffExt = new File("test_single_with_different_ext.html");
        api.entity().demand().export(docId, templates.getRows().get(0), ExportExtension.ods, exportFileDiffExt);
        assertTrue(exportFileDiffExt.exists());
        assertTrue(exportFileDiffExt.length() > 0);
    }

    @Test
    public void test_exportMultiple() throws IOException, ApiClientException {
        String docId;
        {
            Organization ag = api.entity().organization().get().getRows().get(0);
            Store st = api.entity().store().get().getRows().get(0);

            Demand doc = new Demand();
            doc.setAgent(ag);
            doc.setOrganization(ag);
            doc.setStore(st);
            api.entity().demand().create(doc);

            docId = doc.getId();
        }

        ListEntity<Template> templates = api.entity().demand().metadata().embeddedtemplate();
        assertNotNull(templates);
        assertNotNull(templates.getRows());
        assertFalse(templates.getRows().isEmpty());

        File exportFile = new File("test_complect.pdf");
        api.entity().demand().export(
                docId,
                exportFile,
                printRequest(templates.getRows().get(0)),
                printRequest(templates.getRows().get(1), 1),
                printRequest(templates.getRows().get(2), 2)
        );
        assertTrue(exportFile.exists());
        assertTrue(exportFile.length() > 0);
    }
}
