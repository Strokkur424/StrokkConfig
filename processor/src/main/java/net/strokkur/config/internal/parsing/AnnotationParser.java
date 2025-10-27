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
package net.strokkur.config.internal.parsing;

import net.strokkur.config.GenerateConfig;
import net.strokkur.config.internal.exceptions.ProcessorException;
import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.intermediate.ConfigSection;
import net.strokkur.config.internal.intermediate.CustomType;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * A parser for converting annotated source classes into
 * their intermediate internal representation.
 */
public interface AnnotationParser {

  /**
   * Parses a class annotated with {@link GenerateConfig} into an internal {@link ConfigModel} object.
   *
   * @param classElement the class to parse
   * @return the parsed object
   */
  ConfigModel parseClass(TypeElement classElement) throws ProcessorException;

  /**
   * Parses a field into an internal {@link ConfigField}.
   *
   * @param variable the variable to parse
   * @return the parsed object
   */
  ConfigField parseField(TypeElement classElement, VariableElement variable) throws ProcessorException;

  /**
   * Parses a class annotated with {@link net.strokkur.config.CustomType} into an internal {@link CustomType} object.
   *
   * @param classElement the class to parse
   * @return the parsed object
   */
  CustomType parseCustomType(TypeElement classElement) throws ProcessorException;

  /**
   * Parses a subsection of a config into an internal {@link ConfigSection} object.
   *
   * @param classElement the class to parse
   * @return the parsed object
   */
  ConfigSection parseConfigSection(TypeElement classElement) throws ProcessorException;
}
