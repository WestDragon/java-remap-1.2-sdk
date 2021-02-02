package ru.moysklad.remap_1_2.entities.discounts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.moysklad.remap_1_2.entities.MetaEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Discount extends MetaEntity {
    /**
     * Индикатор, является ли скидка активной на данный момент
     */
    private Boolean active;

    /**
     * Индикатор, действует ли скидка на всех агентов
     */
    private Boolean allAgents;

    /**
     * Теги контрагентов, к которым применяется скидка (если применяется не ко всем контрагентам)
     */
    private List<String> agentTags;

    public Discount(String id) {
        super(id);
    }
}
