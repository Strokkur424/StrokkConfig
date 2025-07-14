package net.strokkur.config.internal.intermediate;

import javax.lang.model.element.ExecutableElement;

public interface CustomSerializers {

    ExecutableElement getSerializationMethod();

    ExecutableElement getDeserializationMethod();
}
