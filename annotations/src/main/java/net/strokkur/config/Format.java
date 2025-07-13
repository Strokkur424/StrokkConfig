package net.strokkur.config;

/**
 * The format of the configuration file.
 */
public enum Format {

    /**
     * Yaml Ain't Markup Language.
     * <p>
     * This variant uses <a href="https://github.com/SpongePowered/Configurate">Configurate</a>.
     */
    YAML_CONFIGURATE,

    /**
     * Yaml Ain't Markup Language.
     * <p>
     * This variant uses <a href="https://github.com/snakeyaml/snakeyaml">SnakeYAML</a>.
     */
    YAML_SNAKEYAML,

    /**
     * JavaScript Object Notation.
     * <p>
     * This format uses the <a href="https://github.com/google/gson">gson</a> library.
     */
    JSON,

    /**
     * Human-Optimized Config Object Notation.
     * <p>
     * This format uses the <a href="https://github.com/SpongePowered/Configurate">Configurate</a> library.
     */
    HOCON
}
