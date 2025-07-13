package net.strokkur.config.internal.impl.fields;

import net.strokkur.config.internal.intermediate.FieldType;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.Set;

public class DefaultFieldTypeImpl implements FieldType {

    private final TypeMirror type;
    private final @Nullable Element element;

    public DefaultFieldTypeImpl(TypeMirror type, Elements elementUtils) {
        String name = type.toString().split("<")[0];
        this.type = type;
        this.element = elementUtils.getTypeElement(name);
    }

    @Override
    public String getClassString() {
        return type.toString();
    }

    @Override
    public String getClassName() {
        if (element == null) {
            // Primitive type
            return getClassString();
        }
        
        return parseClassName(element);
    }

    @Override
    public Set<String> getImports() {
        if (element == null) {
            // We do not have to import primitives
            return Set.of();
        }

        return Set.of(
            getClassString().split("<")[0]
        );
    }
}
