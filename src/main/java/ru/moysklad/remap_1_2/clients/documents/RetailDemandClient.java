package ru.moysklad.remap_1_2.clients.documents;

import ru.moysklad.remap_1_2.ApiClient;
import ru.moysklad.remap_1_2.clients.EntityClientBase;
import ru.moysklad.remap_1_2.clients.endpoints.*;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.entities.documents.DocumentPosition;
import ru.moysklad.remap_1_2.entities.documents.RetailDemand;
import ru.moysklad.remap_1_2.entities.documents.positions.RetailSalesDocumentPosition;
import ru.moysklad.remap_1_2.responses.metadata.MetadataAttributeSharedStatesResponse;

public final class RetailDemandClient
        extends EntityClientBase
        implements
        GetListEndpoint<RetailDemand>,
        PostEndpoint<RetailDemand>,
        DeleteByIdEndpoint,
        DocumentMetadataEndpoint<MetadataAttributeSharedStatesResponse>,
        MetadataAttributeEndpoint,
        GetByIdEndpoint<RetailDemand>,
        PutByIdEndpoint<RetailDemand>,
        MassCreateUpdateDeleteEndpoint<RetailDemand>,
        DocumentPositionsEndpoint<RetailSalesDocumentPosition>,
        ExportEndpoint,
        PublicationEndpoint,
        HasStatesEndpoint,
        HasFilesEndpoint<RetailDemand> {

    public RetailDemandClient(ApiClient api) {
        super(api, "/entity/retaildemand/");
    }

    @Override
    public Class<? extends MetaEntity> entityClass() {
        return RetailDemand.class;
    }

    @Override
    public Class<? extends MetaEntity> metaEntityClass() {
        return MetadataAttributeSharedStatesResponse.class;
    }

    @Override
    public Class<? extends DocumentPosition> documentPositionClass() {
        return RetailSalesDocumentPosition.class;
    }
}
