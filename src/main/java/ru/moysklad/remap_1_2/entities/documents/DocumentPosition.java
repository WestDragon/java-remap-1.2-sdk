package ru.moysklad.remap_1_2.entities.documents;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.entities.products.Product;
import ru.moysklad.remap_1_2.entities.products.markers.ProductMarker;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DocumentPosition extends MetaEntity {
    private ProductMarker assortment;
    private Product.ProductPack pack;
    private Double price;
    private Double quantity;
    private Stock stock;

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public class Stock{
        private Double cost;
        private Double quantity;
        private Double reserve;
        private Double intransit;
        private Double available;
    }
}
