package net.strokkur.config.annotations;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to declare the parse method of a field.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface CustomParse {

    /**
     * The method name of the method to use for parsing this field. Must be declared in the same class as the field.
     * <p>
     * The method's first parameter must be of the same type as the annotated field.
     */
    @Language("jvm-method-name")
    String value();
}
