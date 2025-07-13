package net.strokkur.config.internal.impl;

import net.strokkur.config.internal.intermediate.ConfigFormat;
import net.strokkur.config.internal.intermediate.ConfigMetadata;
import org.jspecify.annotations.Nullable;

public class ConfigMetadataImpl implements ConfigMetadata {

    private final String originalClass;
    private final String packageName;
    private final String interfaceClassName;
    private final ConfigFormat configFormat;
    private final @Nullable String filePath;
    private final boolean defaultNonNull;

    public ConfigMetadataImpl(String originalClass, String packageName, String interfaceClassName, ConfigFormat configFormat, @Nullable String filePath, boolean defaultNonNull) {
        this.originalClass = originalClass;
        this.packageName = packageName;
        this.interfaceClassName = interfaceClassName;
        this.configFormat = configFormat;
        this.filePath = filePath;
        this.defaultNonNull = defaultNonNull;
    }

    @Override
    public String getFilePath() {
        return filePath == null ? "config" + configFormat.defaultExtension() : filePath;
    }

    @Override
    public String getPackage() {
        return packageName;
    }

    @Override
    public String getOriginalClass() {
        return originalClass;
    }
    
    @Override
    public String getInterfaceClass() {
        return interfaceClassName;
    }

    @Override
    public String getImplementationClass() {
        return interfaceClassName + "Impl";
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
