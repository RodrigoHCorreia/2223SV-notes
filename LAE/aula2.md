# Aula 2 - Evolução Histórica, Java vs Kotlin, Kotlin-to-JVM, .class e bytecodes
> **Os objetivos aprendidos não são exclusivos do Java.**
> Reflexão é dos pontos mais importantes para o primeiro trabalho

## Java Type System 

### Porque Java

- Porque ao trabalhar em **java estamos muito mais perto** do sistema de tipos do java (Java TS), comparando com kotlin.
- 1 linha em kotlin gerou 5 membros, essa linha em java a relação é muito mais fácil de fazer a transição para Java TS.
- Java usado para fazer a biblioteca que temos de fazer para o autorouter e Kotlin para os testes unitários.

# Java vs Kotlin

## Kotlin-to-JVM

**Student.kt**
```kotlin
class Student(val nr: Int, val name: String)
```

**App.kt**
```kotlin
fun main(){
    println(Student(1, "Alberto"))
}
```

* Para ambos é necessário indicar o classpath onde se indica a class pertendida.
* **bytecode** - Instruções dentro das funções
* **Metadata** - Estrutura
* **membro** - pode ser um campo, uma propriedade ou um construtor, qualquer coisa que exista dentro de uma classe.
  * Estes membros têm uma acessibilidade associada, pode ser:
    * .private - Não é acessível de fora.
    * .package
    * .protected
    * .public

### .class

#### Kotlin
> kotlinc -cp . file.kt (varios files separados por ; em win em vez de : em unix) . represenata a propria diretoria
> kotlin -cp . file (; em linux em vez de .)
* Ao compilar Student.kt cria um Student.class, ao compilar App.kt cria um AppKt.class (**porque o mesmo não tem uma class então o compiler cria uma class com o nome do file+sufixo.**)
* Ao correr Student.class, apresenta os **membros** públicos

#### Java

* Ao correr Student.class com -p para gerar também o que é privado, temos **5 membros**:
  * 2 **funções** (public final int getNr() e public final java.lang.String getName())
  * 1 **construtor**
  * 2 **campos** - FIELD

* **NO JAVA TYPE SYSTEM NÃO EXISTE GETTER E NÃO EXISTE PROPRIEDADES** (Muito importante)

##### TPC: correr dataclass Student(val nr: Int, val name: String) e reportar o output obtido.

### Bytecodes

- Em **bytecodes** aparecem diferentes **invokes**:
  - invoke static
  - invoke special
  - invoke interface
  - invoke virtual
  - invoke dynamic

>javap -c file.class

**App.kt**
```kotlin
fun main(){
    val s = Student(...)
    println(s.nr)
    s.print()
}
```

- ao usar **java** para correr, não conseguimos correr algo com acesso às bibliotecas auxiliares do kotlin por omissão, para isso temos de incluir também o path da biblioteca do kotlin usada do seguinte modo:
> java -cp '.:/Users..../Kotlin/lib/*' AppKt

- Ambos o s.nr como o s.print geram invokevirtual pois são ambos considerados métodos.
- o construtor é um invokespecial, em bytecode a função construtora fica sempre com "<init>"

## Posters (frases mais importantes na ótica do professor)

* **Os objetivos aprendidos não são exclusivos do Java.**
* **membro é qualquer coisa que exista dentro de uma classe.**
* **NO JAVA TYPE SYSTEM NÃO EXISTE GETTER E NÃO EXISTE PROPRIEDADES.**
* **Só os campos (FIELDS) é que ocupam memória.** (COM DIREITO A PERGUNTA NO EXAME SOBRE O TEMA)
* A diferença do java para o kotlin (comando de consola para correr) é que o kotlin preenche as libs que utiliza por omissão e não é preciso indicar o classpath.
