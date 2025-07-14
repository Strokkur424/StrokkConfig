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
package net.strokkur.config.internal.parsing;

import net.strokkur.config.Format;
import net.strokkur.config.annotations.ConfigFilePath;
import net.strokkur.config.annotations.ConfigNullable;
import net.strokkur.config.annotations.CustomType;
import net.strokkur.config.annotations.CustomTypeReturn;
import net.strokkur.config.annotations.GenerateConfig;
import net.strokkur.config.internal.exceptions.ProcessorException;
import net.strokkur.config.internal.impl.ConfigMetadataImpl;
import net.strokkur.config.internal.impl.ConfigModelImpl;
import net.strokkur.config.internal.impl.fields.ConfigFieldImpl;
import net.strokkur.config.internal.impl.fields.ConfigSectionImpl;
import net.strokkur.config.internal.impl.fields.CustomTypeImpl;
import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigFormat;
import net.strokkur.config.internal.intermediate.ConfigMetadata;
import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.intermediate.ConfigSection;
import net.strokkur.config.internal.intermediate.FieldType;
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class AnnotationParserImpl implements AnnotationParser {

    private final MessagerWrapper messager;
    private final Elements elementUtils;
    private final Types typesUtil;

    public AnnotationParserImpl(MessagerWrapper messager, Elements elementUtils, Types typesUtil) {
        this.messager = messager;
        this.elementUtils = elementUtils;
        this.typesUtil = typesUtil;
    }

    @Override
    @NullUnmarked
    public @NonNull ConfigModel parseClass(@NonNull TypeElement classElement) throws ProcessorException {
        ConfigMetadata metadata = getMetadata(classElement);
        ConfigModel.Builder builder = ConfigModelImpl.builder(metadata);

        for (Element element : classElement.getEnclosedElements()) {
            if (element instanceof TypeElement typeElement) {
                if (element.getAnnotation(CustomType.class) != null) {
                    builder.addCustomType(parseCustomType(typeElement));
                    continue;
                }

                builder.addSection(parseConfigSection(typeElement));
                continue;
            }

            if (element instanceof VariableElement variable) {
                builder.addField(parseField(classElement, variable));
            }
        }

        return builder.build();
    }

    @Override
    @NullUnmarked
    public @NonNull ConfigField parseField(@NonNull TypeElement classElement, @NonNull VariableElement variable) throws ProcessorException {
        ConfigField.Builder builder = ConfigFieldImpl.builder(
            FieldType.ofTypeMirror(variable.asType(), messager, typesUtil),
            variable.getSimpleName().toString()
        );

        if (!(variable.asType() instanceof DeclaredType declaredType)) {
            // Primitives will never have any custom implementations
            return builder.build();
        }

        Element element = declaredType.asElement();

        // Check if the class is a custom type
        if (element.getAnnotation(CustomType.class) != null) {
            for (Element subElement : element.getEnclosedElements()) {
                if (!(subElement instanceof ExecutableElement methodElement)) {
                    continue;
                }

                if (methodElement.getAnnotation(CustomTypeReturn.class) != null) {
                    // This is the custom method
                    builder.setCustomParseMethod(methodElement);
                    builder.setFieldType(FieldType.ofTypeMirror(methodElement.getReturnType(), messager, typesUtil));

                    for (VariableElement parameter : methodElement.getParameters()) {
                        builder.addMethodParameter(parameter);
                    }
                    builder.setIsVarArgs(methodElement.isVarArgs());

                    return builder.build();
                }
            }
        }

        // Check if the class is an inner class of the config class --> section
        for (Element subElement : classElement.getEnclosedElements()) {
            if (!(subElement instanceof TypeElement innerClassElement)) {
                continue;
            }

            if (innerClassElement == element) {
                // The declared type is a section; therefor we apply our own logic instead of just serializing
                builder.setIsSectionAccessor(true);
                return builder.build();
            }
        }

        return builder.build();
//        
//        CustomParse customParse = variable.getAnnotation(CustomParse.class);
//        ExecutableElement customParseElement = null;
//        String customParseMethodName = customParse != null ? customParse.value() : "";
//        
//        boolean hasCustomType = false;
//
//        TypeMirror variableReturnType = variable.asType();
//        if (variableReturnType instanceof DeclaredType declaredType) {
//            TypeElement typeElement = (TypeElement) typesUtil.asElement(declaredType);
//
//            if (typeElement.getAnnotation(CustomType.class) != null) {
//                // This is a custom type, so we should reflect that.
//                hasCustomType = true;
//                customParseMethodName = 
//            }
//        }
//        
//
//        if (!customParseMethodName.isBlank()) {
//            for (Element element : classElement.getEnclosedElements()) {
//                if (element instanceof ExecutableElement method) {
//                    if (method.getSimpleName().contentEquals(customParseMethodName)) {
//                        customParseElement = method;
//                        break;
//                    }
//                }
//            }
//        }
    }

    @Override
    @NullUnmarked
    public net.strokkur.config.internal.intermediate.@NonNull CustomType parseCustomType(@NonNull TypeElement classElement) throws ProcessorException {
        CustomTypeImpl impl = new CustomTypeImpl();
        impl.setDefaultNonNull(classElement.getAnnotation(ConfigNullable.class) != null);

        for (Element element : classElement.getEnclosedElements()) {
            if (element instanceof VariableElement parameter) {
                impl.addConfigField(parseField(classElement, parameter));
                continue;
            }

            if (element instanceof ExecutableElement executable && element.getAnnotation(CustomType.class) != null) {
                impl.setReturnElement(executable);
            }
        }

        return impl;
    }

    @Override
    @NullUnmarked
    public @NonNull ConfigSection parseConfigSection(@NonNull TypeElement classElement) throws ProcessorException {
        ConfigSectionImpl impl = new ConfigSectionImpl(classElement.getSimpleName().toString());
        impl.setDefaultNonNull(classElement.getAnnotation(ConfigNullable.class) != null);

        for (Element element : classElement.getEnclosedElements()) {
            if (element instanceof VariableElement parameter) {
                impl.addField(parseField(classElement, parameter));
            }
        }

        return impl;
    }

    @NullUnmarked
    private @NonNull ConfigMetadata getMetadata(@NonNull TypeElement classElement) throws ProcessorException {
        GenerateConfig generateConfig = classElement.getAnnotation(GenerateConfig.class);
        String packageName = ((PackageElement) classElement.getEnclosingElement()).getQualifiedName().toString();

        Format format = Format.HOCON;
        net.strokkur.config.annotations.ConfigFormat configFormatAnnotation = classElement.getAnnotation(net.strokkur.config.annotations.ConfigFormat.class);
        if (configFormatAnnotation != null) {
            format = configFormatAnnotation.value();
        }

        ConfigFilePath filePath = classElement.getAnnotation(ConfigFilePath.class);
        String path = null;
        if (filePath != null && !filePath.value().isBlank()) {
            path = filePath.value();
        }

        boolean defaultNonNull = classElement.getAnnotation(ConfigNullable.class) == null;

        return new ConfigMetadataImpl(
            classElement.getSimpleName().toString(),
            packageName,
            generateConfig.value(),
            ConfigFormat.getFromEnum(format),
            path,
            defaultNonNull
        );
    }
}
