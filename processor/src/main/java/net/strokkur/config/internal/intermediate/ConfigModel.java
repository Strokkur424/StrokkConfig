package net.strokkur.config.internal.intermediate;

import java.util.List;

/**
 * Internal representation of the annotated config model class.
 */
public interface ConfigModel {

    ConfigMetadata getMetadata();

    List<ConfigField> getFields();

    List<CustomType> getCustomTypes();

    List<ConfigSection> getSections();

    interface Builder {
        Builder setMetadata(ConfigMetadata metadata);
        Builder addField(ConfigField field);
        Builder addCustomType(CustomType type);
        Builder addSection(ConfigSection section);

        ConfigModel build();
    }
}
