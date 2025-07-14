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
package net.strokkur.config.internal.intermediate;

import net.strokkur.config.Format;
import net.strokkur.config.internal.exceptions.ProcessorException;
import net.strokkur.config.internal.impl.printer.SourcePrinterCustom;
import net.strokkur.config.internal.impl.printer.SourcePrinterGson;
import net.strokkur.config.internal.impl.printer.SourcePrinterHocon;
import net.strokkur.config.internal.impl.printer.SourcePrinterYamlConfigurate;
import net.strokkur.config.internal.impl.printer.SourcePrinterYamlSnakeYaml;
import net.strokkur.config.internal.printer.SourcePrinter;
import net.strokkur.config.internal.util.MessagerWrapper;

import java.io.Writer;

public interface ConfigFormat {

    static ConfigFormat getFromEnum(Format format) throws ProcessorException {
        return switch (format) {
            case HOCON -> new HoconFormat();
            case JSON_GSON -> new GsonFormat();
            case YAML_CONFIGURATE -> new YamlConfigurateFormat();
            case YAML_SNAKEYAML -> new YamlSnakeYamlFormat();
            case CUSTOM -> new CustomFormat();
            default -> throw new ProcessorException("Failed to find implementation for format '" + format + '.');
        };
    }

    SourcePrinter getSourcesPrinter(Writer writer, ConfigModel configModel, MessagerWrapper messager);
    String defaultExtension();

    class HoconFormat implements ConfigFormat {

        @Override
        public SourcePrinter getSourcesPrinter(Writer writer, ConfigModel configModel, MessagerWrapper messager) {
            return new SourcePrinterHocon(writer, configModel, messager);
        }

        @Override
        public String defaultExtension() {
            return ".conf";
        }
    }

    class GsonFormat implements ConfigFormat {

        @Override
        public SourcePrinter getSourcesPrinter(Writer writer, ConfigModel configModel, MessagerWrapper messager) {
            return new SourcePrinterGson(writer, configModel, messager);
        }

        @Override
        public String defaultExtension() {
            return ".json";
        }
    }

    class YamlConfigurateFormat implements ConfigFormat {

        @Override
        public SourcePrinter getSourcesPrinter(Writer writer, ConfigModel configModel, MessagerWrapper messager) {
            return new SourcePrinterYamlConfigurate(writer, configModel, messager);
        }

        @Override
        public String defaultExtension() {
            return ".yaml";
        }
    }

    class YamlSnakeYamlFormat implements ConfigFormat {

        @Override
        public SourcePrinter getSourcesPrinter(Writer writer, ConfigModel configModel, MessagerWrapper messager) {
            return new SourcePrinterYamlSnakeYaml(writer, configModel, messager);
        }

        @Override
        public String defaultExtension() {
            return ".yaml";
        }
    }

    class CustomFormat implements ConfigFormat {

        @Override
        public SourcePrinter getSourcesPrinter(Writer writer, ConfigModel configModel, MessagerWrapper messager) {
            return new SourcePrinterCustom(writer, configModel, messager);
        }

        @Override
        public String defaultExtension() {
            return ".txt";
        }
    }
}
