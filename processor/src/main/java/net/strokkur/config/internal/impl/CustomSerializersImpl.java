/*
 * StrokkCommands - A super simple annotation based zero-shade Paper configuration library.
 * Copyright (C) 2025 Strokkur24
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <https://www.gnu.org/licenses/>.
 */
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
