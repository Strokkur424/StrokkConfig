package net.strokkur.config.internal.exceptions;

import net.strokkur.config.internal.util.MessagerWrapper;
import org.jspecify.annotations.Nullable;

import javax.lang.model.element.Element;

public class ProcessorException extends Exception {

    private final @Nullable Element element;

    public ProcessorException(String message, @Nullable Element element) {
        super(message);
        this.element = element;
    }

    public ProcessorException(String message) {
        this(message, null);
    }

    public @Nullable Element getElement() {
        return element;
    }

    public void warn(MessagerWrapper messager) {
        if (element != null) {
            messager.warnElement(this.getMessage(), this.getElement());
        } else {
            messager.warn(this.getMessage());
        }
    }

    public void error(MessagerWrapper messager) {
        if (element != null) {
            messager.errorElement(this.getMessage(), this.getElement());
        } else {
            messager.error(this.getMessage());
        }
    }
}
