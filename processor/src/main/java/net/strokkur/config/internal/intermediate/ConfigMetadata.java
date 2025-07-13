package net.strokkur.config.internal.intermediate;

/**
 * Metadata represented by annotations on the config model class.
 */
public interface ConfigMetadata {

    String getFilePath();

    String getClassName();

    ConfigFormat getFormat();

    boolean isDefaultNonNull();
}
