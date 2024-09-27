package ru.moysklad.remap_1_2.entities;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Дополнительное поле
 */

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttributeOperation extends MetaEntity implements Attribute {

    /**
     * Тип доп. поля
     */
    private Type type;

    /**
     * Тип сущности справочника, если тип поля — Справочник
     */
    private Meta.Type entityType;

    /**
     * Значение
     */
    private Object value;

    /**
     * Флажок о том, является ли доп. поле видимым на UI. Не может быть скрытым и обязательным одновременно. Только для операций
     */
    private Boolean show;

    /**
     * Флажок о том, является ли доп. поле обязательным
     */
    private Boolean required;

    /**
     * (для поля типа "Файл") Метаданные файла
     */
    private Meta download;

    /**
     * Метадата сущности справочника
     */
    private Meta customEntityMeta;

    /**
     * Описание
     */
    private String description;

    /**
     * Тип сущности, которой принадлежит аттрибут
     */
    private transient Meta.Type AttributeEntityType;

    public AttributeOperation(String id) {
        super(id);
    }

    public AttributeOperation(Meta.Type AttributeType, String id, Type type, Object value){
        super(id);
        this.type = type;
        this.value = value;
        if (MetaEntity.class.isAssignableFrom(value.getClass())) {
            entityType = Meta.Type.find((MetaEntity) value);
        }
        this.AttributeEntityType = AttributeType;
    }

    public AttributeOperation(Meta.Type AttributeType, String id, MetaEntity value){
        super(id);
        this.value = value;
        entityType = Meta.Type.find(value);
        this.AttributeEntityType = AttributeType;
    }

    public <T> T getValueAs(Class<T> tClass) {
        return (T) value;
    }

}
