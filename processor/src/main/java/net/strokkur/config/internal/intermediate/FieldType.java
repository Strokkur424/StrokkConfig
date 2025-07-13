package net.strokkur.config.internal.intermediate;

import java.util.Set;

/**
 * Internal representation of a config field's type.
 */
public interface FieldType {

    String getFullyQualifiedName();

    String getSimpleNameParameterized();

    Set<String> getImports();
}
