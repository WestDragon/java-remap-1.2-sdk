package ru.moysklad.remap_1_2.utils.params;

import ru.moysklad.remap_1_2.entities.Attribute;
import ru.moysklad.remap_1_2.entities.AttributeOperation;
import ru.moysklad.remap_1_2.entities.Meta;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.utils.MetaHrefUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.moysklad.remap_1_2.utils.Constants.DATE_FORMAT_PATTERN;

public class FilterParam extends ApiParam {
    private final Object key;
    private final Object value;
    private final FilterType filterType;
    private final EntityFilterType entityFilterType;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

    private FilterParam(String key, FilterType filterType, String value) {
        super(Type.filter);
        this.key = key;
        this.value = value;
        this.filterType = filterType;
        this.entityFilterType = EntityFilterType.SIMPLE;
    }

    private FilterParam(Attribute key, FilterType filterType, Object value) {
        super(Type.filter);
        this.key = key;
        this.value = value;
        this.filterType = filterType;
        this.entityFilterType = EntityFilterType.ATTRIBUTE;
    }

    private FilterParam(String key, FilterType filterType, MetaEntity value) {
        super(Type.filter);
        this.key = key;
        this.value = value;
        this.filterType = filterType;
        this.entityFilterType = EntityFilterType.HREF;
    }

    public static FilterParam filterEq(String key, String value) {
        return filter(key, FilterType.equals, value);
    }

    public static FilterParam filterNot(String key, String value) {
        return filter(key, FilterType.not_equals, value);
    }

    public static FilterParam filter(String key, FilterType filterType, String value) {
        return new FilterParam(key, filterType, value);
    }

    public static FilterParam filterEq(Attribute key, Object value) {
        return filter(key, FilterType.equals, value);
    }

    public static FilterParam filterNot(Attribute key, Object value) {
        return filter(key, FilterType.not_equals, value);
    }

    public static FilterParam filter(Attribute key, FilterType filterType, Object value) {
        return new FilterParam(key, filterType, value);
    }

    public static FilterParam filterEq(String key, MetaEntity value) {
        return filter(key, FilterType.equals, value);
    }

    public static FilterParam filterNot(String key, MetaEntity value) {
        return filter(key, FilterType.not_equals, value);
    }

    public static FilterParam filter(String key, FilterType filterType, MetaEntity value) {
        return new FilterParam(key, filterType, value);
    }

    @Override
    protected String render(String host) {
        StringBuilder filterString = new StringBuilder();
        switch (entityFilterType) {
            case SIMPLE:
                filterString.append(key).append(filterType.str).append(value);
                break;
            case HREF:
                MetaEntity hrefValue = (MetaEntity) value;
                filterString.append(key)
                        .append(filterType.str);
                if (hrefValue != null) {
                    filterString.append(MetaHrefUtils.makeHref(Meta.Type.find(hrefValue), hrefValue, host));
                }
                break;
            case ATTRIBUTE:
                Attribute attrKey = (Attribute) key;
                if (attrKey == null) {
                    throw new IllegalArgumentException("key не может быть null");
                } else if (attrKey.getMeta() == null || attrKey.getMeta().getHref() == null) {
                    if (attrKey.getAttributeEntityType() == null) {
                        throw new IllegalArgumentException("key.AttributeType не может быть null");
                    } else if (attrKey.getId() == null) {
                        throw new IllegalArgumentException("key.id не может быть null");
                    }
                    filterString.append(host)
                            .append("/entity/")
                            .append(attrKey.getAttributeEntityType().getApiName())
                            .append("/metadata/attributes/")
                            .append(attrKey.getId())
                            .append(filterType.str);
                } else {
                    filterString.append(attrKey.getMeta().getHref());
                }

                switch (attrKey.getType()) {
                    case stringValue:
                    case longValue:
                    case doubleValue:
                    case fileValue:
                    case booleanValue:
                    case textValue:
                    case linkValue:
                        filterString.append(value.toString());
                        break;
                    case timeValue:
                        filterString.append(formatter.format((LocalDateTime) value));
                        break;
                    default:
                        if (value != null) {
                            if (MetaEntity.class.isAssignableFrom(value.getClass())) {
                                Meta.Type type = attrKey.getEntityType();
                                type = type == null ? Meta.Type.find((MetaEntity) value) : type;
                                filterString.append(MetaHrefUtils.makeHref(type, ((MetaEntity) value), host));
                            } else {
                                throw new IllegalArgumentException("Неизвестный тип данных дополнительного поля: " + value.getClass().getSimpleName());
                            }
                        }
                }
                break;
        }
        return filterString.toString();
    }

    private enum EntityFilterType {
        SIMPLE, HREF, ATTRIBUTE
    }

    public enum FilterType {
        equals("="),
        greater(">"),
        lesser("<"),
        greater_or_equals(">="),
        lesser_or_equals("<="),
        not_equals("!="),
        equivalence("~"),
        equivalence_left("~="),
        equivalence_right("=~");

        private final String str;

        FilterType(String str) {
            this.str = str;
        }

        public String getStr() {
            return str;
        }
    }
}
