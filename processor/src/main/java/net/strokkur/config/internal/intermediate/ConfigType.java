package net.strokkur.config.internal.intermediate;

import java.util.Set;

/**
 * Internal representation of a config field's type.
 */
public interface ConfigType {

    String getFieldType();

    default Set<String> getImports() {
        return Set.of();
    }
}
