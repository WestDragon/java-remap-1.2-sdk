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
public class AttachedFile extends Attachment {
    /**
     * Дата создания
     */
    private LocalDateTime created;

    /**
     * Сотрудник, прикрепивший файл
     */
    private Employee createdBy;
}
