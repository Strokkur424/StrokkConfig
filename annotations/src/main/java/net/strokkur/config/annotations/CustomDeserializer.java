package net.strokkur.config.annotations;

import net.strokkur.config.Format;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation given to a deserialization method for a {@link Format#CUSTOM} format.
 * <p>
 * The annotated method must be at least package-private and the first parameter
 * should be a {@link String} for accepting the serialized content. The method
 * should return the type of the config model class.
 * <p>
 * Example method:
 * <pre>{@code
 * @CustomDeserializer
 * static ConfigModel deserialize(String serialized) throws IOException {
 *     ConfigModel model = new ConfigModel();
 *     model.someValue = serialized;
 *     return model;
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface CustomDeserializer {}
