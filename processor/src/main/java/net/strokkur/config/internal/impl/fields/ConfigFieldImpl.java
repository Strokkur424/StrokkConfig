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
import net.strokkur.config.internal.intermediate.CustomParseMethodType;
import net.strokkur.config.internal.intermediate.FieldType;
import net.strokkur.config.internal.intermediate.Parameter;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ConfigFieldImpl implements ConfigField {

    private final FieldType fieldType;
    private final String fieldName;
    private final @Nullable ExecutableElement customParseMethod;
    private final boolean isNullable;
    private final List<Parameter> methodParameters;
    private final boolean isSectionAccessor;
    private final boolean isVarArgs;
    private final CustomParseMethodType customParseMethodType;

    public ConfigFieldImpl(FieldType fieldType, String fieldName, @Nullable ExecutableElement customParseMethod, boolean isNullable,
                           List<Parameter> methodParameters, boolean isSectionAccessor, boolean isVarArgs, CustomParseMethodType customParseMethodType) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.customParseMethod = customParseMethod;
        this.isNullable = isNullable;
        this.methodParameters = methodParameters;
        this.isSectionAccessor = isSectionAccessor;
        this.isVarArgs = isVarArgs;
        this.customParseMethodType = customParseMethodType;
    }

    public static Builder builder(FieldType fieldType, String fieldName) {
        return new BuilderImpl(fieldType, fieldName);
    }

    @Override
    public List<Parameter> getMethodParameters() {
        return Collections.unmodifiableList(methodParameters);
    }

    @Override
    public FieldType getFieldType() {
        return fieldType;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public @Nullable ExecutableElement getCustomParseMethod() {
        return customParseMethod;
    }

    @Override
    public CustomParseMethodType getCustomParseMethodType() {
        return customParseMethodType;
    }

    @Override
    public boolean isNullable() {
        return isNullable;
    }

    @Override
    public boolean isSectionAccessor() {
        return isSectionAccessor;
    }

    @Override
    public boolean isVarArgs() {
        return isVarArgs;
    }

    private static class BuilderImpl implements Builder {
        private final List<Parameter> methodParameters = new ArrayList<>();
        private FieldType fieldType;
        private String fieldName;
        private boolean nullable = false;
        private boolean isSectionAccessor = false;
        private @Nullable ExecutableElement customParseMethod = null;
        private boolean isVarArgs = false;
        private CustomParseMethodType customParseMethodType = CustomParseMethodType.NONE;

        public BuilderImpl(FieldType fieldType, String fieldName) {
            this.fieldType = fieldType;
            this.fieldName = fieldName;
        }

        @Override
        public Builder setFieldType(FieldType fieldType) {
            this.fieldType = fieldType;
            return this;
        }

        @Override
        public Builder setFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        @Override
        public Builder addMethodParameter(Parameter type) {
            methodParameters.add(type);
            return this;
        }

        @Override
        public Builder setNullable(boolean value) {
            this.nullable = value;
            return this;
        }

        @Override
        public Builder setIsSectionAccessor(boolean value) {
            this.isSectionAccessor = value;
            return this;
        }

        @Override
        public Builder setCustomParseMethod(ExecutableElement customParseMethod) {
            this.customParseMethod = customParseMethod;
            return this;
        }

        @Override
        public Builder setIsVarArgs(boolean value) {
            this.isVarArgs = value;
            return this;
        }

        @Override
        public Builder setCustomParseMethodType(CustomParseMethodType customParseMethodType) {
            this.customParseMethodType = customParseMethodType;
            return this;
        }

        @Override
        public ConfigField build() {
            return new ConfigFieldImpl(
                fieldType, fieldName, customParseMethod, nullable, methodParameters, isSectionAccessor, isVarArgs, customParseMethodType
            );
        }
    }
}
