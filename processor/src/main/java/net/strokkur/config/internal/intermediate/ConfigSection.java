package net.strokkur.config.internal.intermediate;

import java.util.List;

/**
 * Internal representation of a nested, serialized config class.
 */
public interface ConfigSection {

    boolean isDefaultNonNull();

    List<ConfigField> getFields();
}
