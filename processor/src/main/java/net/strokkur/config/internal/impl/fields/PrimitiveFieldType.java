package net.strokkur.config.internal.impl.fields;

import net.strokkur.config.internal.intermediate.FieldType;

import java.util.Set;

public class PrimitiveFieldType implements FieldType {

    private final String primitiveName;

    public PrimitiveFieldType(String primitiveName) {
        this.primitiveName = primitiveName;
    }

    @Override
    public String getFullyQualifiedName() {
        return primitiveName;
    }

    @Override
    public String getSimpleNameParameterized() {
        return primitiveName;
    }

    @Override
    public Set<String> getImports() {
        return Set.of();
    }
}
