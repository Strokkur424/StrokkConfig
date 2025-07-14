package net.strokkur.config.internal.intermediate;

import net.strokkur.config.internal.impl.fields.ObjectFieldType;
import net.strokkur.config.internal.impl.fields.PrimitiveFieldType;
import net.strokkur.config.internal.util.MessagerWrapper;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Set;

/**
 * Internal representation of a config field's type.
 */
public interface FieldType {

    String getFullyQualifiedName();

    String getSimpleNameParameterized();

    Set<String> getImports();

    static FieldType ofTypeMirror(TypeMirror typeMirror, MessagerWrapper messager, Types typesUtil) {
        if (typeMirror.getKind().isPrimitive()) {
            return new PrimitiveFieldType(typeMirror.toString());
        }
        
        return new ObjectFieldType(messager, (DeclaredType) typeMirror, typesUtil);
    }
}
