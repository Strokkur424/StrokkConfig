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

import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

public class SourcePrinterYamlSnakeYaml extends AbstractImplementationSourcePrinter {

    public SourcePrinterYamlSnakeYaml(@Nullable Writer writer, ConfigModel model, MessagerWrapper messager) {
        super(writer, model, messager);
    }

    @Override
    protected Set<String> getImplementationImports() {
        return Set.of(
            "org.yaml.snakeyaml.DumperOptions",
            "org.yaml.snakeyaml.LoaderOptions",
            "org.yaml.snakeyaml.Yaml",
            "org.yaml.snakeyaml.constructor.BaseConstructor",
            "org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor",
            "org.yaml.snakeyaml.introspector.Property",
            "org.yaml.snakeyaml.introspector.PropertyUtils",
            "org.yaml.snakeyaml.nodes.NodeTuple",
            "org.yaml.snakeyaml.nodes.Tag",
            "org.yaml.snakeyaml.representer.Representer",
            "java.lang.annotation.Annotation"
        );
    }

    @Override
    protected void printImplementationDependantReloadImpl() throws IOException {
        printBlock("""
                LoaderOptions loaderOptions = new LoaderOptions();
                loaderOptions.setEnumCaseSensitive(false);
                
                DumperOptions dumperOptions = new DumperOptions();
                dumperOptions.setIndent(2);
                dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                
                BaseConstructor constructor = new CustomClassLoaderConstructor(this.getClass().getClassLoader(), loaderOptions);
                constructor.setPropertyUtils(new KebabCasePropertyUtils());
                
                Representer representer = new KebabCaseRepresenter(dumperOptions);
                
                Yaml yaml = new Yaml(constructor, representer, dumperOptions, loaderOptions);
                
                if (!Files.exists(path)) {
                    // If the file doesn't exist, create it
                    model = new MessagesConfigModel();
                    String yamlString = yaml.dumpAs(model, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
                    Files.createDirectories(path.getParent());
                    Files.writeString(path, yamlString);
                } else {
                    // If the file exists, load it
                    String yamlString = Files.readString(path);
                    model = yaml.loadAs(yamlString, MessagesConfigModel.class);
                }""",
            model.getMetadata().getOriginalClass(),
            model.getMetadata().getOriginalClass()
        );
    }

    @Override
    protected void printExtra() throws IOException {
        println();
        printBlock("""
            //
            // SnakeYAML Utility Classes
            //
            
            /**
             * This class is required for transforming the, by-default, emitted camelCase
             * keys into kebab-case.
             */
            private class KebabCaseRepresenter extends Representer {
            
                public KebabCaseRepresenter(DumperOptions options) {
                    super(options);
                }
            
                @Override
                protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
                    if (property == null) {
                        return null;
                    }
            
                    String originalName = property.getName();
                    String kebabName = toKebabCase(originalName);
            
                    // Replace name for this representation only
                    Property kebabProperty = new Property(kebabName, property.getType()) {
                        @Override
                        public String getName() {
                            return kebabName;
                        }
            
                        @Override
                        public Class<?>[] getActualTypeArguments() {
                            return property.getActualTypeArguments();
                        }
            
                        @Override
                        public void set(Object object, Object value) throws Exception {
                            property.set(object, value);
                        }
            
                        @Override
                        public Object get(Object object) {
                            return property.get(object);
                        }
            
                        @Override
                        public List<Annotation> getAnnotations() {
                            return property.getAnnotations();
                        }
            
                        @Override
                        public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
                            return property.getAnnotation(annotationType);
                        }
                    };
            
                    return super.representJavaBeanProperty(javaBean, kebabProperty, propertyValue, customTag);
                }
            
                private String toKebabCase(String input) {
                    return input.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
                }
            }
            
            /**
             * This class is required for transforming loaded keys
             * back into camelCase so they can be mapped correctly.
             */
            private class KebabCasePropertyUtils extends PropertyUtils {
            
                @Override
                public Property getProperty(Class<?> type, String name) {
                    return super.getProperty(type, toCamelCase(name));
                }
            
                private static String toCamelCase(String kebab) {
                    String[] split = kebab.split("-");
            
                    StringBuilder builder = new StringBuilder(split[0]);
                    for (int i = 1; i < split.length; i++) {
                        builder
                            .append(split[i].substring(0, 1).toUpperCase())
                            .append(split[i].substring(1));
                    }
            
                    return builder.toString();
                }
            }""");
    }
}
