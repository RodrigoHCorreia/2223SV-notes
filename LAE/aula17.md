# Aula 17 - Benchmark continuation and Logger dynamic

## Benchmarking continuation

- Efeito da JVM que altera os valores.
- Como o emptyPrinter não faz nada, a JVM evita o salto para a função, fazendo com que o tempo seja menor do que o que aconteceria.
- para isto podemos usar um `Blackhole` que protege das **otimizações** da JVM, sem usar Overhead.

## Logger dynamic

- **Porque classe abstrata e não interface? **
- Interface só tem metodos abstratos
- Classe abstrata -> pode ter metodos abstratos + campos + metodos concretos
- Classe não abstrata -> pode ter campos + metodos implementados

´´´java
import org.cojen.maker.ClasseMaker;

/*
public class LoggerBaseLineStudent extendes AbstractLogger {

    protected LoggerBaselineStudent(Printer out) {
        super(out);
    }

    public void log(Object target) {
        Student s = (Student) target;
        out.print("Student: ")
        /**
        * Prop nr
        */
        out.print("nr = ");
        out.print(s.getNr());
        out.print(", ");
        /**
        * Prop name
        */
        out.print("name = ");
        out.print(s.getName());
        out.print(", ");

        /**
        * End
        */
        out.println();
    }
}
*/
public class LoggerDynamic{
    public static ClassMaker buildLoggerDynamicForProperties(Class<?> domain){
        ClassMaker clazzMaker = ClassMaker.begin().public_().extends_(AbstractLogger.class);

        MethodMaker ctor = clazzMaker.addConstructor(Printer.class).public_();
        ctor.invokeSuperConstructor(ctor.param(0));

        final var outFieldName = "out";
        MethodMaker logMaker = clazzMaker.addMethod(void.class, "log", Object.class).public_().override();
        Variable target = logMaker.param(0).cast(domain); // Student s = (Student) target;
        logMaker // out.print("Student: ")
            .field(outFieldName)
            .invoke("print", domain.getSimpleName() + ": ");
        
        /**
        * For each property generate: 
        * out.print("name = ");
        * out.print(s.getName());
        * out.print(", ");
        */
        stream(domain
                .getDeclaredMethods())
                .filter(m -> m.getParameterCount() == 0 && m.getName().startsWith("get"))
                .forEach(m -> {
                    final var propName = m.getName().substring(3).toLowerCase();
                    logMaker.field(outFieldName).invoke("print", propName + " = "); 

                    final var propValueVar = target.invoke(m.getName());
                    logMaker.field(outFieldName).invoke("print", propValueVar); // out.print(s.getName());
                    logMaker.field(outFieldName).invoke("print", ", "); // out.print(", ");
                })
        logMaker.field(outFieldName).invoke("println", ""); // out.println();
        return clazzMaker;
    }
}
´´´

## Posters

Dinamica 
    Usamos REFLECT => bytecode
                => bytecode para fazer Reflection está mal