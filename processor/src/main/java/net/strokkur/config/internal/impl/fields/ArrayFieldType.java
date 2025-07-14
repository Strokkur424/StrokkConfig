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

import net.strokkur.config.internal.intermediate.FieldType;
import net.strokkur.config.internal.util.MessagerWrapper;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Set;

public class ArrayFieldType implements FieldType {

    private final FieldType componentFieldType;
    private final int levelsOfNesting;

    public ArrayFieldType(MessagerWrapper messagerWrapper, ArrayType type, Types types) {
        int nesting = 0;
        TypeMirror declared = type;
        while (declared instanceof ArrayType arrayType) {
            declared = arrayType.getComponentType();
            nesting++;
        }

        componentFieldType = FieldType.ofTypeMirror(declared, messagerWrapper, types);
        levelsOfNesting = nesting;
    }

    @Override
    public String getFullyQualifiedName() {
        return componentFieldType.getFullyQualifiedName() + "[]".repeat(levelsOfNesting);
    }

    @Override
    public String getSimpleNameParameterized() {
        return componentFieldType.getSimpleNameParameterized() + "[]".repeat(levelsOfNesting);
    }

    @Override
    public Set<String> getImports() {
        return componentFieldType.getImports();
    }
}
