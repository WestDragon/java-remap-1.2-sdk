package com.lognex.api.clients.documents;

import com.lognex.api.ApiClient;
import com.lognex.api.clients.EntityClientBase;
import com.lognex.api.clients.endpoints.*;
import com.lognex.api.entities.MetaEntity;
import com.lognex.api.entities.documents.Demand;
import com.lognex.api.responses.metadata.MetadataAttributeSharedStatesResponse;

public final class DemandClient
        extends EntityClientBase
        implements
        GetListEndpoint<Demand>,
        PostEndpoint<Demand>,
        DeleteByIdEndpoint,
        DocumentMetadataEndpoint<MetadataAttributeSharedStatesResponse>,
        MetadataAttributeEndpoint,
        DocumentNewEndpoint<Demand>,
        GetByIdEndpoint<Demand>,
        PutByIdEndpoint<Demand>,
        DocumentPositionsEndpoint,
        ExportEndpoint,
        PublicationEndpoint {

    public DemandClient(ApiClient api) {
        super(api, "/entity/demand/");
    }

    @Override
    public Class<? extends MetaEntity> entityClass() {
        return Demand.class;
    }

    @Override
    public Class<? extends MetaEntity> metaEntityClass() {
        return MetadataAttributeSharedStatesResponse.class;
    }
}
