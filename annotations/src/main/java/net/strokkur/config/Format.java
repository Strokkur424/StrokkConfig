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
package net.strokkur.config;

/**
 * The format of the configuration file.
 */
public enum Format {

    /**
     * Yaml Ain't Markup Language.
     * <p>
     * This variant uses <a href="https://github.com/SpongePowered/Configurate">Configurate</a>.
     */
    YAML_CONFIGURATE,

    /**
     * Yaml Ain't Markup Language.
     * <p>
     * This variant uses <a href="https://github.com/snakeyaml/snakeyaml">SnakeYAML</a>.
     */
    YAML_SNAKEYAML,

    /**
     * JavaScript Object Notation.
     * <p>
     * This format uses the <a href="https://github.com/google/gson">gson</a> library.
     */
    JSON,

    /**
     * Human-Optimized Config Object Notation.
     * <p>
     * This format uses the <a href="https://github.com/SpongePowered/Configurate">Configurate</a> library.
     */
    HOCON
}
