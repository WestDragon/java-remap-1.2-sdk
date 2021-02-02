package ru.moysklad.remap_1_2.clients;

import ru.moysklad.remap_1_2.ApiClient;
import ru.moysklad.remap_1_2.clients.endpoints.*;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.entities.Store;
import ru.moysklad.remap_1_2.responses.metadata.MetadataAttributeSharedResponse;

public final class StoreClient
        extends EntityClientBase
        implements
        GetListEndpoint<Store>,
        PostEndpoint<Store>,
        DeleteByIdEndpoint,
        MetadataEndpoint<MetadataAttributeSharedResponse>,
        MetadataAttributeEndpoint,
        GetByIdEndpoint<Store>,
        PutByIdEndpoint<Store>,
        MassCreateUpdateDeleteEndpoint<Store> {

    public StoreClient(ApiClient api) {
        super(api, "/entity/store/");
    }

    @Override
    public Class<? extends MetaEntity> entityClass() {
        return Store.class;
    }

    @Override
    public Class<? extends MetaEntity> metaEntityClass() {
        return MetadataAttributeSharedResponse.class;
    }
}
