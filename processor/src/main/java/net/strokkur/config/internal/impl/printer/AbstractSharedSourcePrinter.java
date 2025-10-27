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
import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.intermediate.Parameter;
import net.strokkur.config.internal.printer.AbstractPrinter;
import net.strokkur.config.internal.printer.SharedSourcePrinter;
import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractSharedSourcePrinter extends AbstractPrinter implements SharedSourcePrinter {

  protected final ConfigModel model;
  protected final MessagerWrapper messager;

  public AbstractSharedSourcePrinter(@Nullable Writer writer, ConfigModel model, MessagerWrapper messager) {
    super(writer);
    this.model = model;
    this.messager = messager;
  }

  protected abstract String getJavaDocInfo();

  protected abstract String seeOther();

  protected abstract String classType();

  protected abstract String className();

  protected String getOopPart() {
    return "";
  }

  @Override
  public void printClassJavaDoc() throws IOException {
    printBlock("""
            /**
             * {}
             *
             * @author Strokkur24 - StrokkConfig
             * @version {}
             * @see {}
             * @see {}
             */""",
        getJavaDocInfo(),
        BuildConstants.VERSION,
        model.getMetadata().getOriginalClass(),
        seeOther()
    );
  }

  @Override
  public void printClassDeclaration() throws IOException {
    printBlock("""
            @NullMarked
            public {} {}{} {""",
        classType(),
        className(),
        getOopPart()
    );
  }

  @Override
  public Set<String> getAllImports() {
    Set<String> imports = new HashSet<>(getStandardImports());

    model.getFields().forEach(field -> {
      imports.addAll(field.getFieldType().getImports());
      for (Parameter methodParam : field.getMethodParameters()) {
        imports.addAll(methodParam.getFieldType().getImports());
      }
    });
    model.getSections().forEach(sec -> sec.getFields().forEach(
        field -> {
          imports.addAll(field.getFieldType().getImports());
          for (Parameter methodParam : field.getMethodParameters()) {
            imports.addAll(methodParam.getFieldType().getImports());
          }
        }
    ));

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
}
