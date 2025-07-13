package net.strokkur.config.annotations;

import net.strokkur.config.Format;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for declaring the used configuration language.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ConfigFormat {

    /**
     * The language. Defaults to {@code Language.HOCON} if not set. 
     */
    Format value() default Format.HOCON;
}
