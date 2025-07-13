package net.strokkur.config.internal.intermediate;

import org.jspecify.annotations.Nullable;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

/**
 * Internal representation of classes annotated with {@link CustomType}.
 */
public interface CustomType extends ConfigType {

    boolean isDefaultNonNull();

    List<ConfigField> getFields();

    @Nullable
    ExecutableElement getTypeReturn();
}
