package ru.moysklad.remap_1_2.clients.endpoints;

import ru.moysklad.remap_1_2.entities.ExportExtension;
import ru.moysklad.remap_1_2.entities.ExportRequest;
import ru.moysklad.remap_1_2.entities.ExportRequest.ExportRequestItem;
import ru.moysklad.remap_1_2.entities.Template;
import ru.moysklad.remap_1_2.utils.ApiClientException;
import ru.moysklad.remap_1_2.utils.HttpRequestExecutor;
import ru.moysklad.remap_1_2.utils.MetaHrefUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static ru.moysklad.remap_1_2.utils.Constants.API_PATH;

public interface ExportEndpoint extends Endpoint {
    @ApiEndpoint
    default void export(String id, Template template, File file) throws IOException, ApiClientException {
        export(id, template, file, false);
    }

    @ApiEndpoint
    default void export(String id, Template template, ExportExtension extension, File file) throws IOException, ApiClientException {
        export(id, template, extension, file, false);
    }

    @ApiEndpoint
    default void export(String id, File file, PrintRequest... printRequests) throws IOException, ApiClientException {
        export(id, file, false, printRequests);
    }

    @ApiEndpoint
    default void export(String id, ExportRequest exportRequest, File file) throws IOException, ApiClientException {
        export(id, exportRequest, file, false);
    }

    @ApiEndpoint
    default void export(String id, Template template, File file, boolean addPrintDocumentContentHeader) throws IOException, ApiClientException {
        ExportExtension parsedExtension = ExportExtension.extractFromFile(file);
        export(id, template, parsedExtension, file, addPrintDocumentContentHeader);
    }

    @ApiEndpoint
    default void export(String id, Template template, ExportExtension extension, File file, boolean addPrintDocumentContentHeader) throws IOException, ApiClientException {
        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setTemplate(template);
        exportRequest.setExtension(extension);
        export(id, exportRequest, file, addPrintDocumentContentHeader);
    }

    @ApiEndpoint
    default void export(String id, File file, boolean addPrintDocumentContentHeader, PrintRequest... printRequests) throws IOException, ApiClientException {
        ExportRequest exportRequest = new ExportRequest();

        exportRequest.setTemplates(new ArrayList<>());
        for (PrintRequest printRequest : printRequests) {
            ExportRequestItem eri = new ExportRequestItem();
            eri.setTemplate(printRequest.template);
            eri.setCount(printRequest.count);
            exportRequest.getTemplates().add(eri);
        }

        export(id, exportRequest, file, addPrintDocumentContentHeader);
    }

    @ApiEndpoint
    default void export(String id, ExportRequest exportRequest, File file, boolean addPrintDocumentContentHeader) throws IOException, ApiClientException {
        if (exportRequest.getTemplate() != null) {
            MetaHrefUtils.fillMeta(exportRequest.getTemplate(), api().getHost() + API_PATH);
        } else if (exportRequest.getTemplates() != null) {
            exportRequest.getTemplates()
                .forEach(t -> MetaHrefUtils.fillMeta(t.getTemplate(), api().getHost() + API_PATH));
        }
        HttpRequestExecutor req = HttpRequestExecutor.
                path(api(), path() + id + "/export/").
                body(exportRequest);

        if (addPrintDocumentContentHeader) req.header("X-Lognex-Get-Content", "true");

        req.postAndSaveTo(file);
    }

    public static class PrintRequest {
        private Template template;
        private int count;

        private PrintRequest(Template template, int count) {
            this.template = template;
            this.count = count;
        }

        public static PrintRequest printRequest(Template template) {
            return printRequest(template, 1);
        }

        public static PrintRequest printRequest(Template template, int count) {
            return new PrintRequest(template, count);
        }
    }
}
