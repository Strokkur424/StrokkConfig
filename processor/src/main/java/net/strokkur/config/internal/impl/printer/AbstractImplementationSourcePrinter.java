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
package net.strokkur.config.internal.impl.printer;

import net.strokkur.config.internal.impl.fields.ArrayFieldType;
import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.intermediate.ConfigSection;
import net.strokkur.config.internal.intermediate.CustomParseMethodType;
import net.strokkur.config.internal.intermediate.Parameter;
import net.strokkur.config.internal.printer.ImplementationSourcePrinter;
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.ExecutableElement;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

public abstract class AbstractImplementationSourcePrinter extends AbstractSharedSourcePrinter implements ImplementationSourcePrinter {

    public AbstractImplementationSourcePrinter(@Nullable Writer writer, ConfigModel model, MessagerWrapper messager) {
        super(writer, model, messager);
    }

    abstract Set<String> getImplementationImports();

    protected void printExtra() throws IOException {}

    @Override
    public Set<String> getAllImports() {
        Set<String> out = super.getAllImports();
        out.addAll(getImplementationImports());
        return out;
    }

    @Override
    protected String getJavaDocInfo() {
        return "The generated implementation for the {@link %s} interface.".formatted(model.getMetadata().getInterfaceClass());
    }

    @Override
    protected String seeOther() {
        return model.getMetadata().getInterfaceClass();
    }

    @Override
    protected String className() {
        return model.getMetadata().getImplementationClass();
    }

    @Override
    protected String getOopPart() {
        return " implements " + model.getMetadata().getInterfaceClass();
    }

    @Override
    protected String classType() {
        return "class";
    }

    @Override
    public void print() throws IOException {
        printPackage();
        println();

        printImports();
        println();

        printClassJavaDoc();
        printClassDeclaration();

        incrementIndent();
        println();

        printModelVariable();
        println();
        printNestedSectionsVariables();

        printUtilityMethods();
        println();

        printReloadMethodImplementation();
        println();

        printAccessMethods();

        printNestedClasses();
        
        printExtra();
        
        decrementIndent();
        println("}");
    }

    @Override
    public void printModelVariable() throws IOException {
        printBlock("""
                @Nullable
                private {} model = null;""",
            model.getMetadata().getOriginalClass()
        );
    }

    @Override
    public void printNestedSectionsVariables() throws IOException {
        for (ConfigSection section : model.getSections()) {
            println("@Nullable");
            println("private {} {}Model = null;",
                section.getSectionName(),
                section.getSectionName().substring(0, 1).toLowerCase() + section.getSectionName().substring(1)
            );
            println();
        }
    }

    @Override
    public void printUtilityMethods() throws IOException {
        printBlockNoJava("""
            //
            // Validation utility
            //
            
            private static <M> M validateLoaded(@Nullable M model) {
                if (model == null) {
                    throw new IllegalStateException("The config file '" + FILE_PATH + "' is not fully loaded.");
                }
                return model;
            }
            
            private static <M, T> T getNonNull(M model, Function<M, T> function, String name) {
                T result = function.apply(model);
                if (result == null) {
                    throw new IllegalStateException("The value for '" + name + "' in the config file '" + FILE_PATH + "' is not declared.");
                }
                return result;
            }""");
    }

    @Override
    public void printReloadMethodImplementation() throws IOException {
        printBlock("""
            //
            // Reloading
            //
            
            @Override
            public void reload(JavaPlugin plugin, String filePath) throws IOException {
                final Path path = plugin.getDataPath().resolve(filePath);
            
                if (!Files.exists(path) && plugin.getResource(filePath) != null) {
                    plugin.saveResource(filePath, false);
                }""");
        println();
        incrementIndent();

        printImplementationDependantReloadImpl();
        println();

        printBlock("""
            if (model == null) {
                // If the model is still null, throw an exception
                throw new IOException("Failed to load configuration model for '" + path + "'.");
            }""");

        for (ConfigSection section : model.getSections()) {
            String sectionClassName = section.getSectionName();
            String sectionVariableName = section.getSectionName().substring(0, 1).toLowerCase() + section.getSectionName().substring(1);

            println();
            printBlock("""
                    if (model.{} == null) {
                        throw new IOException("Failed to load '{}' section for '" + path + "'.");
                    }
                    
                    {}Model = new {}Impl(model.{});""",
                sectionVariableName,
                sectionVariableName,
                sectionVariableName,
                sectionClassName,
                sectionVariableName
            );
        }

        decrementIndent();
        println("}");
    }

    protected abstract void printImplementationDependantReloadImpl() throws IOException;

    @Override
    public void printAccessMethods() throws IOException {
        printBlock("""
            //
            // Access methods
            //""");
        println();

        List<ConfigField> fields = model.getFields();
        for (int i = 0; i < fields.size(); i++) {
            ConfigField field = fields.get(i);
            if (field.isSectionAccessor()) {
                continue;
            }

            printAccessMethodImpl(field, "model");

            if (i + 1 < fields.size()) {
                println();
            }
        }
    }

    @Override
    public void printNestedClasses() throws IOException {
        if (model.getSections().isEmpty()) {
            return;
        }

        printBlock("""
            //
            // Nested classes
            //""");
        println();

        for (ConfigSection section : model.getSections()) {
            String sectionClassName = section.getSectionName();
            String sectionVariableName = section.getSectionName().substring(0, 1).toLowerCase() + section.getSectionName().substring(1);

            printBlock("""
                    @Override
                    public {} {}() {
                        return validateLoaded(this.{}Model);
                    }""",
                sectionClassName,
                sectionVariableName,
                sectionVariableName
            );

            println();

            println("private record {}Impl({}.{} handle) implements {} {",
                sectionClassName,
                model.getMetadata().getOriginalClass(),
                sectionClassName,
                sectionClassName
            );
            println();
            incrementIndent();

            for (ConfigField field : section.getFields()) {
                printAccessMethodImpl(field, "handle");
            }

            decrementIndent();
            println("}");
        }
    }

    private void printAccessMethodImpl(ConfigField field, String modelVariable) throws IOException {
        String type = field.getFieldType().getSimpleNameParameterized();
        String name = field.getFieldName();

        StringBuilder paramBuilder = new StringBuilder();
        List<Parameter> methodParameters = field.getMethodParameters();
        for (int i = 0; i < methodParameters.size(); i++) {
            Parameter param = methodParameters.get(i);

            String fieldType = param.getFieldType().getSimpleNameParameterized();
            String fieldName = param.getName();

            if (i + 1 == methodParameters.size() && param.getFieldType() instanceof ArrayFieldType) {
                paramBuilder.append(fieldType, 0, fieldType.length() - 2)
                    .append("...")
                    .append(" ")
                    .append(fieldName);
            } else {
                paramBuilder.append(fieldType).append(" ").append(fieldName);
            }

            if (i + 1 < methodParameters.size()) {
                paramBuilder.append(", ");
            }
        }

        println("@Override");
        println("public {} {}({}) {", type, name, paramBuilder);

        incrementIndent();

        StringBuilder builder = new StringBuilder();
        if (!field.isNullable()) {
            builder.append("getNonNull(");
        }

        builder.append("validateLoaded(this.").append(modelVariable).append(")");

        if (!field.isNullable()) {
            builder.append(", m -> m");
        }

        if (field.getCustomParseMethodType() != CustomParseMethodType.CUSTOM_PARSE) {
            builder.append(".");
            builder.append(field.getFieldName());
        }

        ExecutableElement methodElement = field.getCustomParseMethod();
        if (methodElement != null) {
            builder.append(".");
            builder.append(methodElement.getSimpleName().toString());
            builder.append("(");

            if (field.getCustomParseMethodType() == CustomParseMethodType.CUSTOM_PARSE) {
                builder.append("m.").append(field.getFieldName());

                if (!methodParameters.isEmpty()) {
                    builder.append(", ");
                }
            }

            for (int i = 0; i < methodParameters.size(); i++) {
                Parameter param = methodParameters.get(i);
                builder.append(param.getName());
                if (i + 1 < methodParameters.size()) {
                    builder.append(", ");
                }
            }

            builder.append(")");
        }

        if (!field.isNullable()) {
            builder.append(", ").append('"').append(field.getFieldNameDashed()).append('"').append(")");
        }

        println("return {};", builder);
        decrementIndent();

        println("}");
    }
}



