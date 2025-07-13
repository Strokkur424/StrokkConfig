package net.strokkur.config.internal.impl.printer;

import net.strokkur.config.internal.BuildConstants;
import net.strokkur.config.internal.impl.fields.ConfigFieldImpl;
import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.intermediate.ConfigSection;
import net.strokkur.config.internal.printer.AbstractPrinter;
import net.strokkur.config.internal.printer.InterfaceSourcePrinter;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InterfaceSourcePrinterImpl extends AbstractPrinter implements InterfaceSourcePrinter {

    private final ConfigModel model;

    public InterfaceSourcePrinterImpl(@Nullable Writer writer, ConfigModel model) {
        super(writer);
        this.model = model;
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

        decrementIndent();
        println("}");
    }

    @Override
    public Set<String> getAllImports() {
        Set<String> imports = new HashSet<>(STANDARD_INTERFACE_IMPORTS);

        model.getFields().forEach(field -> imports.addAll(field.getFieldType().getImports()));
        
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
//
//        for (ConfigSection section : model.getSections()) {
//            printAccessMethod(new ConfigFieldImpl(
//                
//                section.getFullyQualifiedName().toLowerCase()
//            ));
//            println();
//            printSectionInterface(section);
//        }
    }

    @Override
    public void printAccessMethod(ConfigField field) throws IOException {
        String type = field.getFieldType().getSimpleNameParameterized();
        String name = field.getFieldName();

        StringBuilder paramBuilder = new StringBuilder();
        if (field.getCustomParseMethod() != null) {
            List<? extends VariableElement> parameters = field.getCustomParseMethod().getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                VariableElement param = parameters.get(i);
                paramBuilder.append(param.asType().toString()).append(" ").append(param);

                if (i + 1 < parameters.size()) {
                    paramBuilder.append(", ");
                }
            }
        }

        println("{} {}({});", type, name, paramBuilder);
    }

    @Override
    public void printSectionInterface(ConfigSection type) throws IOException {
//        String name = type.getClassString();
//        type.getFields()
//        
//        println("""
//            interface {} {
//                {} {}({});
//            }""",
//            
//            );
    }
}
