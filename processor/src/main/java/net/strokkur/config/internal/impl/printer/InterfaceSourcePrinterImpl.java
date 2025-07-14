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
import net.strokkur.config.internal.intermediate.Parameter;
import net.strokkur.config.internal.printer.InterfaceSourcePrinter;
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class InterfaceSourcePrinterImpl extends AbstractSharedSourcePrinter implements InterfaceSourcePrinter {

    public InterfaceSourcePrinterImpl(@Nullable Writer writer, ConfigModel model, MessagerWrapper messager) {
        super(writer, model, messager);
    }

    @Override
    public void print() throws IOException {
        printPackage();
        println();
        printImports();
        println();

        printClassJavaDoc();
        printClassDeclaration();
        println();

        incrementIndent();

        printFilePath();
        println();

        printReloading();
        println();

        printAccessMethods();
        printNestedInterfaces();

        decrementIndent();
        println("}");
    }

    @Override
    protected String getJavaDocInfo() {
        return "A config access interface generated from {@link {}}.";
    }

    @Override
    protected String className() {
        return model.getMetadata().getInterfaceClass();
    }

    @Override
    protected String seeOther() {
        return model.getMetadata().getImplementationClass();
    }

    @Override
    protected String classType() {
        return "interface";
    }

    @Override
    public void printFilePath() throws IOException {
        printBlock("""
                //
                // Declared in the @ConfigFileName annotation.
                //
                
                String FILE_PATH = "{}";""",
            model.getMetadata().getFilePath());
    }

    @Override
    public void printReloading() throws IOException {
        printBlock("""
            //
            // Reloading
            //
            
            /**
             * Reload the config file. This method uses the plugin's data path
             * as the target directory and resolves that with the {@link #FILE_PATH}.
             *
             * @param plugin plugin
             */
            default void reload(JavaPlugin plugin) throws IOException {
                reload(plugin, FILE_PATH);
            }
            
            /**
             * Reload the config file. This method uses the plugin's data path
             * as the target directory and resolves that with the provided file path.
             *
             * @param plugin   plugin
             * @param filePath path to resolve the data path with
             */
            void reload(JavaPlugin plugin, String filePath) throws IOException;""");
    }

    @Override
    public void printAccessMethods() throws IOException {
        printBlock("""
            //
            // Access methods
            //
            """);

        println();

        for (ConfigField field : model.getFields()) {
            if (field.isSectionAccessor()) {
                // We handle those below
                continue;
            }

            printAccessMethod(field);
            println();
        }
    }

    @Override
    public void printNestedInterfaces() throws IOException {
        printBlock("""
            //
            // Nested classes
            //
            """);
        println();

        for (ConfigSection section : model.getSections()) {
            println("{} {}();",
                section.getSectionName(),
                section.getSectionName().substring(0, 1).toLowerCase() + section.getSectionName().substring(1)
            );
            printSectionInterface(section);
        }
    }

    @Override
    public void printAccessMethod(ConfigField field) throws IOException {
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
                    .append(param.getName());
            } else {
                paramBuilder.append(fieldType).append(" ").append(fieldName);
            }

            if (i + 1 < methodParameters.size()) {
                paramBuilder.append(", ");
            }
        }

        println("{} {}({});", type, name, paramBuilder);
    }

    @Override
    public void printSectionInterface(ConfigSection type) throws IOException {
        println();
        println("interface {} {", type.getSectionName());
        incrementIndent();

        for (ConfigField field : type.getFields()) {
            printAccessMethod(field);
        }

        decrementIndent();
        println("}");
    }
}
