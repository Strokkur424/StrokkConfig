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
package net.strokkur.config.internal.impl.fields;

import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.CustomType;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomTypeImpl implements CustomType {

    private final List<ConfigField> fields = new ArrayList<>();
    private @Nullable ExecutableElement returnElement = null;
    private boolean nonNull = true;

    public void setReturnElement(ExecutableElement element) {
        this.returnElement = element;
    }

    public void addConfigField(ConfigField field) {
        fields.add(field);
    }

    @Override
    public boolean isDefaultNonNull() {
        return nonNull;
    }

    public void setDefaultNonNull(boolean value) {
        this.nonNull = value;
    }

    @Override
    public List<ConfigField> getFields() {
        return Collections.unmodifiableList(fields);
    }

    @Override
    public @Nullable ExecutableElement getTypeReturn() {
        return returnElement;
    }
}
