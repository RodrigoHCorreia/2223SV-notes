# Aula 16 - Benchmarking JMH

## Benchmarking

- Aplicado ao SW, é a análise de eficiência, desempenho e performance.
  - Medições?? 
  - Melhorar??

- **JMH** - Java Microbenchmark Harness

## Exemplo e resolução de diversos problemas

```java

fun main() {
    val logger = Logger()


    val s = Student(3414, "Zé manel")
    val t0 = System.currentTimeMillis()
    logger.log(s)
    val t1 = System.currentTimeMillis()
    val elapsed = t1 - t0
    println("Logger Reflect log() took $elapsed millis")

}

class LoggerBaselineStudent {

    private final Printer printer;

    public LoggerBaselineStudent(Printer printer) {
        this.printer = printer;
    }

    log(Object target) {
        Student s = (Student) target;
        printer.print("Student: nr = ");
        printer.print(s.getNr());
        printer.print(", name = ");
        printer.print(s.getName());
        printer.print(", \n");
    }
}
```

- Dependência do hardware e do ambiente => ter um Baseline, usar um valor relativo e não um absoluto
- Fatores externos e.g. G.C., Jitter, etc. => Incluir um warmup (descartar as primeiras iterações)
- Precisão -> ms não é suficiente => mais iterações e usar ns
- I.O. => retirar
- Retirar o overhead do domínio 
  - O logger baseline é específico para cada classe pois não usa refleção
- NÃO usar IDE => Consola JVM (porque o IDE pode ter um overhead)

- A medição pode ser feita em:
  - Duração - quanto tempo demora uma operação
  - Throughput - número de operações por unidade de tempo