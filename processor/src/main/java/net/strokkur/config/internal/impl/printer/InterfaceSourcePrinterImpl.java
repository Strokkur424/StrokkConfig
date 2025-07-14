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

import net.strokkur.config.internal.BuildConstants;
import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.intermediate.ConfigSection;
import net.strokkur.config.internal.intermediate.FieldType;
import net.strokkur.config.internal.printer.AbstractPrinter;
import net.strokkur.config.internal.printer.InterfaceSourcePrinter;
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.Nullable;

import javax.annotation.processing.Messager;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InterfaceSourcePrinterImpl extends AbstractPrinter implements InterfaceSourcePrinter {

    private final ConfigModel model;
    private final MessagerWrapper messager;
    private final Types types;

    public InterfaceSourcePrinterImpl(@Nullable Writer writer, ConfigModel model, MessagerWrapper messager, Types types) {
        super(writer);
        this.model = model;
        this.messager = messager;
        this.types = types;
    }

    @Override
    public void print() throws IOException {
        printPackage();
        println();
        printImports();
        println();

        printInterfaceJavaDoc();
        printInterfaceDeclaration();
        println();

        incrementIndent();

        printFilePath();
        println();

        printReloading();
        println();

        printAccessMethods();
        println();
        printNestedInterfaces();

        decrementIndent();
        println("}");
    }

    @Override
    public Set<String> getAllImports() {
        Set<String> imports = new HashSet<>(STANDARD_INTERFACE_IMPORTS);

        model.getFields().forEach(field -> {
            imports.addAll(field.getFieldType().getImports());
            for (VariableElement methodParam : field.getMethodParameters()) {
//                imports.addAll(FieldType.ofTypeMirror(methodParam.asType(), messager, types).getImports());
            }
        });
        model.getSections().forEach(sec -> sec.getFields().forEach(field -> imports.addAll(field.getFieldType().getImports())));
        

        imports.removeIf(str -> str.startsWith(model.getMetadata().getPackage()));
        imports.removeIf(str -> str.startsWith("java.lang."));
        return imports;
    }

    @Override
    public void printPackage() throws IOException {
        println("package {};", model.getMetadata().getPackage());
    }

    @Override
    public void printImports() throws IOException {
        Map<Boolean, List<String>> splitImports = getAllImports().stream()
            .sorted()
            .collect(Collectors.partitioningBy(str -> str.startsWith("java")));

        List<String> javaImports = splitImports.get(true);
        List<String> otherImports = splitImports.get(false);

        for (String i : otherImports) {
            println("import {};", i);
        }

        println();

        for (String i : javaImports) {
            println("import {};", i);
        }
    }

    @Override
    public void printInterfaceJavaDoc() throws IOException {
        printBlock("""
                /**
                 * A config access interface generated from {@link {}}.
                 *
                 * @author Strokkur24 - StrokkConfig
                 * @version {}
                 * @see {}
                 * @see {}
                 */""",
            model.getMetadata().getOriginalClass(),
            BuildConstants.VERSION,
            model.getMetadata().getOriginalClass(),
            model.getMetadata().getImplementationClass()
        );
    }

    @Override
    public void printInterfaceDeclaration() throws IOException {
        printBlock("""
                @NullMarked
                public interface {} {""",
            model.getMetadata().getInterfaceClass()
        );
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

        for (ConfigSection section : model.getSections()) {
            printSectionInterface(section);
        }
    }

    @Override
    public void printAccessMethod(ConfigField field) throws IOException {
        String type = field.getFieldType().getSimpleNameParameterized();
        String name = field.getFieldName();

        StringBuilder paramBuilder = new StringBuilder();
        List<VariableElement> methodParameters = field.getMethodParameters();
        for (int i = 0; i < methodParameters.size(); i++) {
            VariableElement param = methodParameters.get(i);
            
            if (i + 1 == methodParameters.size() && param.asType().getKind() == TypeKind.ARRAY) {
                String typeName = param.asType().toString();
                
                paramBuilder.append(typeName, 0, typeName.length() - 2)
                    .append("...")
                    .append(" ")
                    .append(param);
            } else {
                paramBuilder.append(param.asType().toString()).append(" ").append(param);
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
