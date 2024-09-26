package ru.moysklad.remap_1_2.clients;

import ru.moysklad.remap_1_2.clients.endpoints.*;
import ru.moysklad.remap_1_2.entities.BonusTransaction;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.responses.metadata.MetadataAttributeOperationSharedResponse;

public final class BonusTransactionClient
        extends EntityClientBase
        implements
        GetListEndpoint<BonusTransaction>,
        PostEndpoint<BonusTransaction>,
        MetadataEndpoint<MetadataAttributeOperationSharedResponse>,
        MetadataAttributeOperationEndpoint,
        GetByIdEndpoint<BonusTransaction>,
        PutByIdEndpoint<BonusTransaction>,
        MassCreateUpdateDeleteEndpoint<BonusTransaction>,
        DeleteByIdEndpoint {

    public BonusTransactionClient(ru.moysklad.remap_1_2.ApiClient api) {
        super(api, "/entity/bonustransaction/");
    }

    @Override
    public Class<? extends MetaEntity> entityClass() {
        return BonusTransaction.class;
    }

    @Override
    public Class<? extends MetaEntity> metaEntityClass() {
        return MetadataAttributeOperationSharedResponse.class;
    }
}
