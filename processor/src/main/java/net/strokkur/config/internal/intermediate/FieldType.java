package net.strokkur.config.internal.intermediate;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Set;

/**
 * Internal representation of a config field's type.
 */
public interface FieldType {

    String getClassString();

    String getClassName();

    default String parseClassName(Element element) {
        // Special cases
        if (element.asType().getKind().isPrimitive()) {
            return getClassString();
        }

        Element packageElement = element;
        int limit = 20;
        do {
            packageElement = packageElement.getEnclosingElement();
            if (limit-- < 0) {
                throw new IllegalStateException("Failed to find package for " + getClassString());
            }
        } while (!(packageElement instanceof PackageElement));

        String packageName = ((PackageElement) packageElement).getQualifiedName().toString();
        String withoutQualification = getClassString().substring(packageName.length() + 1);

//        if (element.asType() instanceof DeclaredType dec) {
//            List<? extends TypeMirror> parameterized = dec.getTypeArguments();
//            for (TypeMirror type : parameterized) {
//                String realName = parseClassName(types.asElement(type), types);
//            }
//        }
        
        return withoutQualification;
    }

    default Set<String> getImports() {
        return Set.of(getClassString());
    }
}
