package net.strokkur.config.annotations;

import net.strokkur.config.YamlLibrary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the library used for (de)serialization of a yaml-formatted configuration file.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface YamlConfigLibrary {

    /**
     * The {@link YamlLibrary} to use.
     */
    YamlLibrary value() default YamlLibrary.CONFIGURATE;
}
