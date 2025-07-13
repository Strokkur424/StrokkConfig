package net.strokkur.config.internal.parsing;

import net.strokkur.config.Format;
import net.strokkur.config.annotations.ConfigFilePath;
import net.strokkur.config.annotations.ConfigNullable;
import net.strokkur.config.annotations.CustomParse;
import net.strokkur.config.annotations.CustomType;
import net.strokkur.config.annotations.GenerateConfig;
import net.strokkur.config.internal.exceptions.ProcessorException;
import net.strokkur.config.internal.impl.ConfigMetadataImpl;
import net.strokkur.config.internal.impl.ConfigModelImpl;
import net.strokkur.config.internal.impl.fields.ConfigFieldImpl;
import net.strokkur.config.internal.impl.fields.ConfigSectionImpl;
import net.strokkur.config.internal.impl.fields.CustomTypeImpl;
import net.strokkur.config.internal.impl.fields.ObjectFieldTypeImpl;
import net.strokkur.config.internal.impl.fields.PrimitiveFieldType;
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
        CustomParse customParse = variable.getAnnotation(CustomParse.class);
        ExecutableElement customParseElement = null;
        String customParseMethodName = customParse != null ? customParse.value() : "";

        if (!customParseMethodName.isBlank()) {
            for (Element element : classElement.getEnclosedElements()) {
                if (element instanceof ExecutableElement method) {
                    if (method.getSimpleName().contentEquals(customParseMethodName)) {
                        customParseElement = method;
                        break;
                    }
                }
            }
        }

        FieldType type;
        if (variable.asType().getKind().isPrimitive()) {
            type = new PrimitiveFieldType(variable.asType().toString());
        } else {
            type = new ObjectFieldTypeImpl(messager, (DeclaredType) variable.asType(), typesUtil);
        }

        return new ConfigFieldImpl(
            type,
            variable.getSimpleName().toString(),
            customParseElement
        );
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
        ConfigSectionImpl impl = new ConfigSectionImpl();
        impl.setDefaultNonNull(classElement.getAnnotation(ConfigNullable.class) != null);

        for (Element element : classElement.getEnclosedElements()) {
            if (element instanceof VariableElement parameter) {
                impl.addConfigField(parseField(classElement, parameter));
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
