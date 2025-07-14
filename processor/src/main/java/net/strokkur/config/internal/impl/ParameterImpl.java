package net.strokkur.config.internal.impl;

import net.strokkur.config.internal.intermediate.FieldType;
import net.strokkur.config.internal.intermediate.Parameter;

public class ParameterImpl implements Parameter {

    private final FieldType fieldType;
    private final String fieldName;

    public ParameterImpl(FieldType fieldType, String fieldName) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
    }

    @Override
    public FieldType getFieldType() {
        return fieldType;
    }

    @Override
    public String getName() {
        return fieldName;
    }
}
