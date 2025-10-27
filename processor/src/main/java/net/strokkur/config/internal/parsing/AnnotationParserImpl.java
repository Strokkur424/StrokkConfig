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

import net.strokkur.config.ConfigFilePath;
import net.strokkur.config.ConfigNullable;
import net.strokkur.config.CustomDeserializer;
import net.strokkur.config.CustomParse;
import net.strokkur.config.CustomSerializer;
import net.strokkur.config.CustomType;
import net.strokkur.config.CustomTypeReturn;
import net.strokkur.config.GenerateConfig;
import net.strokkur.config.internal.exceptions.ProcessorException;
import net.strokkur.config.internal.impl.ConfigMetadataImpl;
import net.strokkur.config.internal.impl.ConfigModelImpl;
import net.strokkur.config.internal.impl.CustomSerializersImpl;
import net.strokkur.config.internal.impl.ParameterImpl;
import net.strokkur.config.internal.impl.fields.ConfigFieldImpl;
import net.strokkur.config.internal.impl.fields.ConfigSectionImpl;
import net.strokkur.config.internal.impl.fields.CustomTypeImpl;
import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigFormat;
import net.strokkur.config.internal.intermediate.ConfigMetadata;
import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.intermediate.ConfigSection;
import net.strokkur.config.internal.intermediate.CustomParseMethodType;
import net.strokkur.config.internal.intermediate.CustomSerializers;
import net.strokkur.config.internal.intermediate.FieldType;
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Objects;

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

    CustomParse customParse = variable.getAnnotation(CustomParse.class);
    if (customParse != null) {
      for (Element enclosedElement : classElement.getEnclosedElements()) {
        if (!(enclosedElement instanceof ExecutableElement methodElement)) {
          continue;
        }

        if (methodElement.getSimpleName().contentEquals(customParse.value())) {
          return populateCustomParseMethod(builder, methodElement, 1, CustomParseMethodType.CUSTOM_PARSE);
        }
      }
    }

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
          return populateCustomParseMethod(builder, methodElement, 0, CustomParseMethodType.CUSTOM_TYPE);
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
  }

  @NonNull
  private ConfigField populateCustomParseMethod(ConfigField.Builder builder, ExecutableElement methodElement, int firstParam, CustomParseMethodType type) {
    builder.setCustomParseMethod(methodElement);
    builder.setFieldType(FieldType.ofTypeMirror(methodElement.getReturnType(), messager, typesUtil));

    List<? extends VariableElement> parameters = methodElement.getParameters();
    for (int i = firstParam; i < parameters.size(); i++) {
      VariableElement parameter = parameters.get(i);
      builder.addMethodParameter(new ParameterImpl(
          FieldType.ofTypeMirror(parameter.asType(), messager, typesUtil),
          parameter.getSimpleName().toString()
      ));
    }

    builder.setCustomParseMethodType(type);
    builder.setIsVarArgs(methodElement.isVarArgs());
    return builder.build();
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

    net.strokkur.config.ConfigFormat.Format format = net.strokkur.config.ConfigFormat.Format.HOCON;
    net.strokkur.config.ConfigFormat configFormatAnnotation = classElement.getAnnotation(net.strokkur.config.ConfigFormat.class);
    if (configFormatAnnotation != null) {
      format = configFormatAnnotation.value();
    }

    ConfigFilePath filePath = classElement.getAnnotation(ConfigFilePath.class);
    String path = null;
    if (filePath != null && !filePath.value().isBlank()) {
      path = filePath.value();
    }

    CustomSerializers customSerializers = null;
    if (format == net.strokkur.config.ConfigFormat.Format.CUSTOM) {
      ExecutableElement serializerMethod = null;
      ExecutableElement deserializerMethod = null;

      for (Element element : classElement.getEnclosedElements()) {
        if (!(element instanceof ExecutableElement methodElement) || !methodElement.getModifiers().contains(Modifier.STATIC)) {
          continue;
        }

        if (methodElement.getAnnotation(CustomSerializer.class) != null) {
          if (serializerMethod != null) {
            throw new ProcessorException("Duplicate declaration of serializer method", methodElement);
          }

          if (!Objects.equals(methodElement.getReturnType(), elementUtils.getTypeElement("java.lang.String").asType())) {
            throw new ProcessorException("Invalid return type for serializer method. Must be java.lang.String", methodElement);
          }

          List<? extends VariableElement> params = methodElement.getParameters();
          if (params.size() != 1) {
            throw new ProcessorException("Invalid number of parameters for serializer method. Must contain only one", methodElement);
          }

          if (!Objects.equals(params.getFirst().asType(), classElement.asType())) {
            throw new ProcessorException("Invalid parameters for serializer method. Must be " + classElement.getQualifiedName().toString(), params.getFirst());
          }

          serializerMethod = methodElement;
          continue;
        }

        if (methodElement.getAnnotation(CustomDeserializer.class) != null) {
          if (deserializerMethod != null) {
            throw new ProcessorException("Duplicate declaration of deserializer method", methodElement);
          }

          if (!Objects.equals(methodElement.getReturnType(), classElement.asType())) {
            throw new ProcessorException("Invalid return type for deserializer method. Must be " + classElement.getQualifiedName().toString(), methodElement);
          }

          List<? extends VariableElement> params = methodElement.getParameters();
          if (params.size() != 1) {
            throw new ProcessorException("Invalid number of parameters for deserializer method. Must contain only one", methodElement);
          }

          if (!Objects.equals(params.getFirst().asType(), elementUtils.getTypeElement("java.lang.String").asType())) {
            throw new ProcessorException("Invalid parameters for deserializer method. Must be java.lang.String", params.getFirst());
          }

          deserializerMethod = methodElement;
        }
      }

      if (serializerMethod == null || deserializerMethod == null) {
        throw new ProcessorException("A class with a custom format must include static methods annotated with @CustomSerializer and @CustomDeserializer!", classElement);
      }

      customSerializers = new CustomSerializersImpl(serializerMethod, deserializerMethod);
    }

    boolean defaultNonNull = classElement.getAnnotation(ConfigNullable.class) == null;

    String modelClassName = classElement.getSimpleName().toString();
    String newClassName = generateConfig.value().isBlank()
        ? modelClassName.endsWith("Model") ? modelClassName.substring(0, modelClassName.length() - 5) : modelClassName + "Config"
        : generateConfig.value();

    return new ConfigMetadataImpl(
        modelClassName,
        packageName,
        newClassName,
        ConfigFormat.getFromEnum(format),
        path,
        defaultNonNull,
        customSerializers
    );
  }
}
