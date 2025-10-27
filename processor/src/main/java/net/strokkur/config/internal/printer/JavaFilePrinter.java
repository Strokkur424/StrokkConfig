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
package net.strokkur.config.internal.printer;

import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.util.MessagerWrapper;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.function.Function;

public class JavaFilePrinter {

  private final ConfigModel model;
  private final Function<Writer, SourcePrinter> sourcePrinter;
  private final String sourceName;
  private final Filer filer;
  private final MessagerWrapper messagerWrapper;

  public JavaFilePrinter(ConfigModel model, Function<Writer, SourcePrinter> sourcePrinter, String sourcePath, Filer filer, MessagerWrapper messagerWrapper) {
    this.model = model;
    this.sourcePrinter = sourcePrinter;
    this.sourceName = sourcePath;
    this.filer = filer;
    this.messagerWrapper = messagerWrapper;
  }

  @SuppressWarnings("CallToPrintStackTrace")
  public void print() {
    try {
      String targetImplementationFile = model.getMetadata().getPackage() + "." + sourceName;
      JavaFileObject interfaceObj = filer.createSourceFile(targetImplementationFile);

      try (PrintWriter writer = new PrintWriter(interfaceObj.openWriter())) {
        sourcePrinter.apply(writer).print();
      }
    } catch (Exception e) {
      messagerWrapper.error("Fatal exception occurred while printing source file for {}: {}", sourceName, e.getMessage());
      e.printStackTrace();
    }
  }
}
