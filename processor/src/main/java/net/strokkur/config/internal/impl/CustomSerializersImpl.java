package net.strokkur.config.internal.impl;

import net.strokkur.config.internal.intermediate.CustomSerializers;

import javax.lang.model.element.ExecutableElement;

public class CustomSerializersImpl implements CustomSerializers {
    
    private final ExecutableElement serializationMethod;
    private final ExecutableElement deserializationMethod;

    public CustomSerializersImpl(ExecutableElement serializationMethod, ExecutableElement deserializationMethod) {
        this.serializationMethod = serializationMethod;
        this.deserializationMethod = deserializationMethod;
    }

    @Override
    public ExecutableElement getSerializationMethod() {
        return serializationMethod;
    }

    @Override
    public ExecutableElement getDeserializationMethod() {
        return deserializationMethod;
    }
}
