package net.strokkur.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate a configurable class to generate configuration logic for it.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface GenerateConfig {

    /**
     * The name of the generated configuration interface.
     */
    String value() default "";
}
