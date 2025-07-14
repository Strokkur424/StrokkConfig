package net.strokkur.config.internal.intermediate;

import java.util.List;

/**
 * Internal representation of a nested, serialized config class.
 */
public interface ConfigSection {
    
    String getSectionName();

    boolean isDefaultNonNull();

    void addField(ConfigField field);
    
    List<ConfigField> getFields();
}
