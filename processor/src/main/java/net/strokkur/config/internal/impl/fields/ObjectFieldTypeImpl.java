package net.strokkur.config.internal.impl.fields;

import net.strokkur.config.internal.intermediate.FieldType;
import net.strokkur.config.internal.util.MessagerWrapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectFieldTypeImpl implements FieldType {

    private final MessagerWrapper messagerWrapper;
    private final DeclaredType type;
    private final TypeElement element;
    private final Types types;

    public ObjectFieldTypeImpl(MessagerWrapper messagerWrapper, DeclaredType type, Types types) {
        this.messagerWrapper = messagerWrapper;
        this.type = type;
        this.element = (TypeElement) types.asElement(type);
        this.types = types;
    }

    @Override
    public String getFullyQualifiedName() {
        return element.getQualifiedName().toString();
    }

    @Override
    public String getSimpleNameParameterized() {
        return getSimpleNameRecursive(element, type);
    }

    private String getSimpleNameRecursive(Element element, DeclaredType type) {
        String simpleName = getFullyQualifiedName().substring(getPackage(element).length() + 1);

        List<? extends TypeMirror> parameters = type.getTypeArguments();
        if (parameters.isEmpty()) {
            return simpleName;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            TypeMirror param = parameters.get(i);
            if (!(param instanceof DeclaredType declared)) {
                messagerWrapper.warnElement("Parameter {} is not a DeclaredType", types.asElement(param), param);
                continue;
            }

            builder.append(getSimpleNameRecursive(types.asElement(param), declared));
            if (i + 1 < parameters.size()) {
                builder.append(", ");
            }
        }

        return simpleName + "<" + builder + ">";
    }


    @Override
    public Set<String> getImports() {
        Set<String> out = new HashSet<>();
        getImportsRecursive(out, element, type);
        return out;
    }

    private void getImportsRecursive(Set<String> set, TypeElement element, DeclaredType type) {
        set.add(element.getQualifiedName().toString());

        List<? extends TypeMirror> parameters = type.getTypeArguments();
        if (parameters.isEmpty()) {
            return;
        }

        for (TypeMirror param : parameters) {
            Element paramElement = types.asElement(param);
            if (!(paramElement instanceof TypeElement declaredTypeElement)) {
                messagerWrapper.warnElement("Parameter element {} is not a TypeElement", paramElement, param);
                continue;
            }

            if (!(param instanceof DeclaredType declared)) {
                messagerWrapper.warnElement("Parameter type {} is not a DeclaredType", types.asElement(param), param);
                continue;
            }

            getImportsRecursive(set, declaredTypeElement, declared);
        }
    }
    
    private String getPackage(Element element) {
        do {
            element = element.getEnclosingElement();
        } while (!(element instanceof PackageElement pkgElement));
        
        return pkgElement.getQualifiedName().toString();
    }
}
