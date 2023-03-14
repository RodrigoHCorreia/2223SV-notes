# Aula 5 - Companion object in java; Singleton in java; Kotlin to Java; Proggraming with Metadata

- **Singleton** - padrão de desenho para a compilação de kotlin para java
  - Objecto que só pode ser instanciado uma vez, para isso devemos declarar o construtor como privado.

```java
public class App{

    public static void main(String[] args) {
        Classroom.print();
    }
}
class Classroom{
    public static void print(){
        System.out.println("I am a Classroom");
    }

}
```

- Em java não existem funções de extensão

Em kotlin:
```kotlin
class Person(val nr: Int, val name: String)

fun Person.print(){
    println(this)
}
```

- esta tradução para bytecode cria uma nova classe com o nome do ficheiro, neste caso PersonKt, e dentro desta classe temos o método print, que é um método estático, e que recebe como parâmetro um objecto do tipo Person.

Em java:

```java
public class Person {
    final int nr;
    final String name;

    public Person(int nr, String name) {
        this.nr = nr;
        this.name = name;
    }

    public void print() {
        System.out.println(this);
    }
}
```

## Programming with Metadata

> Programming with Metadata <=> Metadata at runtime <=> Reflection

- Reflection <=	API sobre a metadata

Tipos que representam a metadata e métodos para navegar na metadata

Instância de class é o representante de um tipo (classe, interface, ...)

**getReturnType() : Class** - retorna a classe que representa o tipo de retorno do método

em java todas as classes herdam de Object, e a classe Object define 4 metodos: finalize, equals, hashCode, toString e o getClass.

- Diferentes classLoaders:
  - bootstrap classloader - carrega classes para por a funcionar a JVM
  - application classloader - carrega as classes da classpath
  - url classloader - outros URLs != classpath

- Checked exceptions - são exceptions que são verificadas em tempo de compilação, e que são obrigatórias de serem tratadas, caso não sejam tratadas o código não compila.
- n podemos usar expressões que possam lançar exceções em lambdas.

### TPC 
- a class method tem um método invoke, que recebe como parâmetro o reciever e os argumentos, e retorna um objecto do tipo object, que é o resultado da invocação do método, caso o método seja estático o reciever é null, usar o getModifiers() & Modifier.STATIC para verificar se o método é estático.

## Posters

- A API de reflexão só dá **representantes** de tipos e classes.
- sobre um **parêmetro**, chamando getType() obtemos a classe que representa o tipo do parametro, chamando getClass() obtemos a classe que representa a instancia do parametro.
- A maquina virtual só tem um representante para cada tipo durante a sua execução.
- Classes, Campos, Métodos, Construtores, são todos membros.
- 