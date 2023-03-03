# Aula 3 - Java-to-JVM, .class e bytecodes

**Student.java**
```java
public class Student {
    private final int nr; // <=> val
    private String name; // <=> var

    public Student(int nr, String name) {
        this.nr = nr;
        .name = name;
    }

    public int getNr() {
        return nr;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        this.name = v;
    }

    public int getNameLength() {
        return name.length();
    }

    public void print(){
        System.out.println(this);
    }
}
```

```java
public class App {
    public static void main(String[] args) {
        System.out.println(new Student(1, "Alberto"));
    }
}
```

* Ao compilar Student.java cria um Student.class, que corremos com o javap -p Student.class obtemos:
 
* Var final => Imutável
* campo final => Imutável
* método final => Não Virtual => Não pode ser OVERRIDE

* Encontramos uma diferença nas omissões do java e do kotlin, por omissão o kotlin faz a função e as classes ser final, o java não.
  * Isto existe porque o espaço ocupado por um método final é menor que o de um método virtual, também conhecido como penalizar os métodos.

* Isto torna o java mais chato porque temos de escrever muito mais porém é muito mais facil compreender a passagem de java para o java TS, algo bastante importante na cadeira.
* Existe **interoperabilidade entre java e kotlin a nivel da VM**, pelo facto de ser a mesma, por isso podemos ter uma classe java e uma classe kotlin a interagir entre si, realizada na linkagem (tempo de execução).

**Peças importantes**: classloader, jitter, refleção, etc.

* O classloader é o que carrega as classes para a memória, o jitter é o que vai compilar as classes para bytecodes, e informa o classloader que necessita de carregar a classe para a memória.

## Unmanaged World (e.g. C)

num mundo unmanaged é necessário ter um ficheiro .h e um ficheiro .c, o .h é o header file (Estrutura) e o .c é o source file (Implementação), o point.o é o object file, depois é necessário ligar o point.o com o App.o para criar o executável.

A linkagem feita num managed world é feita pela VM somente no tempo de execução.

* **Linkagem lazy** - carregamento tardio 


## Posters (frases mais importantes na ótica do professor)

Mundo Unmanaged != Managed, 
* Managed tem VM, Jitter, classloader, etc.
  * Componente de SW autosuficiente, que pode ser passado entre programadores sem ser preciso acesso ao código fonte. 
* Unmanaged não tem nada disso, é tudo feito pelo programador.
* só é preciso indicar o classpath quando compilamos algo que tem dependências externas.