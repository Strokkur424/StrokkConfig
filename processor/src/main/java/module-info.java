import org.jspecify.annotations.NullMarked;

@NullMarked
module net.strokkur.config.processor {
  requires java.compiler;
  requires net.strokkur.config.annotations;
  requires org.jetbrains.annotations;
  requires org.jspecify;
}