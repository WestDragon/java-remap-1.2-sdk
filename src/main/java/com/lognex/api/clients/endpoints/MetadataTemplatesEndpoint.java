package com.lognex.api.clients.endpoints;

import com.lognex.api.entities.Attribute;
import com.lognex.api.entities.Meta;
import com.lognex.api.entities.Template;
import com.lognex.api.responses.ListEntity;
import com.lognex.api.utils.ApiClientException;
import com.lognex.api.utils.HttpRequestExecutor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public interface MetadataTemplatesEndpoint extends Endpoint {
    @ApiEndpoint
    default ListEntity<Template> embeddedtemplate() throws IOException, ApiClientException {
        ListEntity<Template> templates = HttpRequestExecutor.
                path(api(), path() + (path().endsWith("metadata/") ? "" : "metadata/") + "embeddedtemplate/").
                list(Template.class);
        return setEntityType(templates);
    }

    @ApiEndpoint
    default Template embeddedtemplate(String id) throws IOException, ApiClientException {
        Template template = HttpRequestExecutor.
                path(api(), path() + (path().endsWith("metadata/") ? "" : "metadata/") + "embeddedtemplate/" + id).
                get(Template.class);
        Meta.Type entityType = getEntityType();
        template.setEntityType(entityType);
        return template;
    }

    @ApiEndpoint
    default ListEntity<Template> customtemplate() throws IOException, ApiClientException {
        ListEntity<Template> templates = HttpRequestExecutor.
                path(api(), path() + (path().endsWith("metadata/") ? "" : "metadata/") + "customtemplate/").
                list(Template.class);
        return setEntityType(templates);
    }

    @ApiEndpoint
    default Template customtemplate(String id) throws IOException, ApiClientException {
        Template template = HttpRequestExecutor.
                path(api(), path() + (path().endsWith("metadata/") ? "" : "metadata/") + "customtemplate/" + id).
                get(Template.class);
        Meta.Type entityType = getEntityType();
        template.setEntityType(entityType);
        return template;
    }

    default ListEntity<Template> setEntityType(ListEntity<Template> templates) {
        Meta.Type entityType = getEntityType();
        List<Template> rows = templates.getRows();
        if (rows != null) {
            templates.setRows(rows.stream().peek(t -> t.setEntityType(entityType)).collect(Collectors.toList()));
        }
        return templates;
    }

    default Meta.Type getEntityType() {
        return Meta.Type.find(path()
                .replace("metadata", "")
                .replace("entity", "")
                .replace("/", "")
        );
    }
}
