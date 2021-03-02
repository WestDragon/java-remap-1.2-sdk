package ru.moysklad.remap_1_2.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.moysklad.remap_1_2.entities.agents.Employee;

import java.time.LocalDateTime;

/**
 * Единица Измерения
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Uom extends MetaEntity {
    /**
     * Дата последнего обновления
     */
    private LocalDateTime updated;

    /**
     * Описание
     */
    private String description;

    /**
     * Код
     */
    private String code;

    /**
     * Внешний код
     */
    private String externalCode;

    /**
     * Флаг Общий доступ
     */
    private Boolean shared;

    /**
     * Сотрудник-владелец
     */
    private Employee owner;

    /**
     * Отдел-владелец
     */
    private Group group;

    public Uom(String id) {
        super(id);
    }
}
