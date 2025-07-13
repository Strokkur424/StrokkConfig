package net.strokkur.config.internal.impl.fields;

import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.CustomType;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomTypeImpl implements CustomType {

    private final TypeElement typeElement;
    private final List<ConfigField> fields = new ArrayList<>();
    private @Nullable ExecutableElement returnElement = null;
    private boolean nonNull = true;

    public CustomTypeImpl(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

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

    @Override
    public String getClassString() {
        return typeElement.toString();
    }

    @Override
    public String getClassName() {
        return parseClassName(typeElement);
    }

    @Override
    public Set<String> getImports() {
        Set<String> imports = new HashSet<>();
        fields.forEach(field -> imports.addAll(field.getFieldType().getImports()));
        
        if (returnElement != null && !returnElement.getReturnType().getKind().isPrimitive()) {
            imports.add(returnElement.getReturnType().toString());
        }
        
        imports.add(getClassString());
        return imports;
    }
}
