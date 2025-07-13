package net.strokkur.config.internal.parsing;

import net.strokkur.config.annotations.GenerateConfig;
import net.strokkur.config.internal.exceptions.ProcessorException;
import net.strokkur.config.internal.intermediate.ConfigField;
import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.intermediate.ConfigSection;
import net.strokkur.config.internal.intermediate.CustomType;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

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
     * Parses a class annotated with {@link net.strokkur.config.annotations.CustomType} into an internal {@link CustomType} object.
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
