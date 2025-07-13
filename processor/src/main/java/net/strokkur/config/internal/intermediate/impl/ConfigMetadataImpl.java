package net.strokkur.config.internal.intermediate.impl;

import net.strokkur.config.internal.intermediate.ConfigFormat;
import net.strokkur.config.internal.intermediate.ConfigMetadata;
import org.jspecify.annotations.Nullable;

public class ConfigMetadataImpl implements ConfigMetadata {

    private final String className;
    private final ConfigFormat configFormat;
    private final @Nullable String filePath;
    private final boolean defaultNonNull;

    public ConfigMetadataImpl(String className, ConfigFormat configFormat, @Nullable String filePath, boolean defaultNonNull) {
        this.className = className;
        this.configFormat = configFormat;
        this.filePath = filePath;
        this.defaultNonNull = defaultNonNull;
    }

    @Override
    public String getFilePath() {
        return filePath == null ? "config" + configFormat.defaultExtension() : filePath;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public ConfigFormat getFormat() {
        return configFormat;
    }

    @Override
    public boolean isDefaultNonNull() {
        return defaultNonNull;
    }
}
