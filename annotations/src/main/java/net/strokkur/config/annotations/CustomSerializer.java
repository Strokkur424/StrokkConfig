package net.strokkur.config.annotations;

import net.strokkur.config.Format;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation given to a serialization method for a {@link Format#CUSTOM} format.
 * <p>
 * The annotated method must be at least package-private and the first parameter
 * should be the type of the config model class for accepting the concrete object. The method
 * should return a {@link String}.
 * <p>
 * Example method:
 * <pre>{@code
 * @CustomSerializer
 * static String serialize(ConfigModel model) throws IOException {
 *     return model.someValue;
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface CustomSerializer {}
