package net.strokkur.config.internal;

import net.strokkur.config.annotations.GenerateConfig;
import net.strokkur.config.internal.exceptions.ProcessorException;
import net.strokkur.config.internal.intermediate.ConfigModel;
import net.strokkur.config.internal.parsing.AnnotationParser;
import net.strokkur.config.internal.parsing.AnnotationParserImpl;
import net.strokkur.config.internal.util.MessagerWrapper;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StrokkConfigProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(GenerateConfig.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_21;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        MessagerWrapper messagerWrapper = MessagerWrapper.wrap(super.processingEnv.getMessager());
        AnnotationParser parser = new AnnotationParserImpl(messagerWrapper);

        Set<? extends Element> annotated = roundEnv.getElementsAnnotatedWith(GenerateConfig.class);
        List<ConfigModel> parsedList = new ArrayList<>(annotated.size());

        for (Element element : annotated) {
            if (!(element instanceof TypeElement classElement)) {
                continue;
            }

            try {
                parsedList.add(parser.parseClass(classElement));
            } catch (ProcessorException e) {
                e.error(messagerWrapper);
            }
        }

//        for (ConfigModel model : parsedList) {
//            try {
//                model.getMetadata().getFormat().getSourcesPrinter().print();
//            } catch (IOException e) {
//                messagerWrapper.error("An internal exception occurred trying to print class file for {}: {}",
//                    model.getMetadata().getFilePath(),
//                    e.getMessage()
//                );
//            }
//        }

        return true;
    }
}
