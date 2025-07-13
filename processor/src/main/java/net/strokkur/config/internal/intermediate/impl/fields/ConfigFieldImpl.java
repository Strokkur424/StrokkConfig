package net.strokkur.config.internal.intermediate.impl.fields;

import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.FieldType;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.ExecutableElement;

public class ConfigFieldImpl implements ConfigField {

    private final FieldType fieldType;
    private final String fieldName;
    private final @Nullable ExecutableElement customParseMethod;

    public ConfigFieldImpl(FieldType fieldType, String fieldName, @Nullable ExecutableElement customParseMethod) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.customParseMethod = customParseMethod;
    }

    public ConfigFieldImpl(FieldType fieldType, String fieldName) {
        this(fieldType, fieldName, null);
    }

    @Override
    public FieldType getFieldType() {
        return fieldType;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public @Nullable ExecutableElement getCustomParseMethod() {
        return customParseMethod;
    }
}
