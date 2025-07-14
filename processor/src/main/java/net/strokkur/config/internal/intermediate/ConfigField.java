package net.strokkur.config.internal.intermediate;

import net.strokkur.config.internal.util.FieldNameContainer;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;

/**
 * Internal representation of a configuration field.
 */
public interface ConfigField extends FieldNameContainer {

    FieldType getFieldType();

    List<VariableElement> getMethodParameters();

    boolean isNullable();

    boolean isSectionAccessor();
    
    boolean isVarArgs();

    @Nullable
    ExecutableElement getCustomParseMethod();

    interface Builder {
        Builder setFieldType(FieldType fieldType);
        Builder setFieldName(String fieldName);
        Builder addMethodParameter(VariableElement type);
        Builder setNullable(boolean value);
        Builder setCustomParseMethod(ExecutableElement customParseMethod);
        Builder setIsSectionAccessor(boolean value);
        Builder setIsVarArgs(boolean value);

        ConfigField build();
    }
}