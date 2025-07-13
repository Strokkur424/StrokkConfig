package net.strokkur.config.internal.impl.fields;

import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.CustomType;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomTypeImpl implements CustomType {

    private final List<ConfigField> fields = new ArrayList<>();
    private @Nullable ExecutableElement returnElement = null;
    private boolean nonNull = true;

    public void setReturnElement(ExecutableElement element) {
        this.returnElement = element;
    }

    public void addConfigField(ConfigField field) {
        fields.add(field);
    }

    @Override
    public boolean isDefaultNonNull() {
        return nonNull;
    }

    public void setDefaultNonNull(boolean value) {
        this.nonNull = value;
    }

    @Override
    public List<ConfigField> getFields() {
        return Collections.unmodifiableList(fields);
    }

    @Override
    public @Nullable ExecutableElement getTypeReturn() {
        return returnElement;
    }
}
