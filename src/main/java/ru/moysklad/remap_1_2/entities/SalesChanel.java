package ru.moysklad.remap_1_2.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.moysklad.remap_1_2.entities.agents.Employee;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SalesChanel extends MetaEntity {

    private Boolean archived;
    private String code;
    private String description;
    private String externalCode;
    private Group group;
    private Employee owner;
    private Boolean shared;
    private Type type;
    private LocalDateTime updated;

    public SalesChanel(String id) {
        super(id);
    }

    public enum Type {
        MESSENGER,
        SOCIAL_NETWORK,
        MARKETPLACE,
        ECOMMERCE,
        CLASSIFIED_ADS,
        DIRECT_SALES,
        OTHER,
        ;
    }
}
