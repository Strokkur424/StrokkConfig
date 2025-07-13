package net.strokkur.config.internal.impl.fields;

import net.strokkur.config.internal.intermediate.FieldType;

public class SectionFieldFieldType implements FieldType {

    private final String fieldName;

    public SectionFieldFieldType(String fieldName) {
        this.fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1).toLowerCase();
    }

    @Override
    public String getClassName() {
        return fieldName; // TODO this makes no sense
    }

    @Override
    public String getClassString() {
        return this.fieldName;
    }
}
