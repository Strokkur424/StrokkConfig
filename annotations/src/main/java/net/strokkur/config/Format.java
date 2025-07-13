package net.strokkur.config;

import net.strokkur.config.annotations.YamlConfigLibrary;

/**
 * The format of the configuration file.
 */
public enum Format {
    
    /**
     * Yaml Ain't Markup Language.
     * <p>
     * You can configure the library using the {@link YamlConfigLibrary} annotation.
     * Defaults to <a href="https://github.com/SpongePowered/Configurate">Configurate</a>.
     */
    YAML,

    /**
     * JavaScript Object Notation.
     * <p>
     * This format always uses the <a href="https://github.com/google/gson">gson</a> library internally.
     */
    JSON,

    /**
     * Human-Optimized Config Object Notation.
     * <p>
     * This format uses the <a href="https://github.com/SpongePowered/Configurate">Configurate</a> library.
     */
    HOCON
}
