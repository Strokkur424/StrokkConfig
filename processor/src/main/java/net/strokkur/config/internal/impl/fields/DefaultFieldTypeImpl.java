package net.strokkur.config.internal.impl.fields;

import net.strokkur.config.internal.intermediate.FieldType;

import javax.lang.model.type.TypeMirror;
import java.util.Set;

public class DefaultFieldTypeImpl implements FieldType {

    private final TypeMirror typeElement;

    public DefaultFieldTypeImpl(TypeMirror typeElement) {
        this.typeElement = typeElement;
    }

    @Override
    public String getClassString() {
        return typeElement.toString();
    }

    @Override
    public Set<String> getImports() {
        if (typeElement.getKind().isPrimitive()) {
            // We do not have to import primitives
            return Set.of();
        }

        return Set.of(
            typeElement.toString()
        );
    }
}
