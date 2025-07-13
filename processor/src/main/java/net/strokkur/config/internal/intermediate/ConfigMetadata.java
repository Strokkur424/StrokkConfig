package net.strokkur.config.internal.intermediate;

/**
 * Metadata represented by annotations on the config model class.
 */
public interface ConfigMetadata {

    String getFilePath();
    
    String getPackage();
    
    String getOriginalClass();
    
    String getInterfaceClass();
    
    String getImplementationClass();

    ConfigFormat getFormat();

    boolean isDefaultNonNull();
}
