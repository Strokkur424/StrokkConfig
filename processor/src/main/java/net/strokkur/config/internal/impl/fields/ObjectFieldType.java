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

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectFieldType implements FieldType {

    private final MessagerWrapper messagerWrapper;
    private final DeclaredType type;
    private final TypeElement element;
    private final Types types;

    public ObjectFieldType(MessagerWrapper messagerWrapper, DeclaredType type, Types types) {
        this.messagerWrapper = messagerWrapper;
        this.type = type;
        this.element = (TypeElement) types.asElement(type);
        this.types = types;
    }

    @Override
    public String getFullyQualifiedName() {
        return element.getQualifiedName().toString();
    }

    @Override
    public String getSimpleNameParameterized() {
        return getSimpleNameRecursive(element, type);
    }

    private String getSimpleNameRecursive(TypeElement element, DeclaredType type) {
        String simpleName = element.getQualifiedName().toString().substring(getPackage(element).length() + 1);

        List<? extends TypeMirror> parameters = type.getTypeArguments();
        if (parameters.isEmpty()) {
            return simpleName;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            TypeMirror param = parameters.get(i);

            Element paramElement = types.asElement(param);
            if (!(paramElement instanceof TypeElement declaredTypeElement)) {
                messagerWrapper.warnElement("Parameter element {} is not a TypeElement", paramElement, param);
                continue;
            }

            if (!(param instanceof DeclaredType declared)) {
                messagerWrapper.warnElement("Parameter type {} is not a DeclaredType", declaredTypeElement, param);
                continue;
            }

            builder.append(getSimpleNameRecursive(declaredTypeElement, declared));
            if (i + 1 < parameters.size()) {
                builder.append(", ");
            }
        }

        return simpleName + "<" + builder + ">";
    }


    @Override
    public Set<String> getImports() {
        Set<String> out = new HashSet<>();
        getImportsRecursive(out, element, type);
        return out;
    }

    private void getImportsRecursive(Set<String> set, TypeElement element, DeclaredType type) {
        set.add(element.getQualifiedName().toString());

        List<? extends TypeMirror> parameters = type.getTypeArguments();
        if (parameters.isEmpty()) {
            return;
        }

        for (TypeMirror param : parameters) {
            Element paramElement = types.asElement(param);
            if (!(paramElement instanceof TypeElement declaredTypeElement)) {
                messagerWrapper.warnElement("Parameter element {} is not a TypeElement", paramElement, param);
                continue;
            }

            if (!(param instanceof DeclaredType declared)) {
                messagerWrapper.warnElement("Parameter type {} is not a DeclaredType", declaredTypeElement, param);
                continue;
            }

            getImportsRecursive(set, declaredTypeElement, declared);
        }
    }

    private String getPackage(Element element) {
        do {
            element = element.getEnclosingElement();
        } while (!(element instanceof PackageElement pkgElement));

        return pkgElement.getQualifiedName().toString();
    }
}
