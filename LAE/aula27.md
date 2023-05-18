# Aula 27 - Sequences

listagem de alunos em ficheiro para o resources

ClassLoader usado obter URI do ficheiro
File(uri) usado para obter o ficheiro e readLines() para obter as linhas do ficheiro

**Sequences** - sequências de dados com uma ordem (e.g. IEnumerable, java.util.Stream, Iterable, Array)

API:
 - Traverse
 - Access

Collection(não se aplica somente a coleções) pipeline API - oferece na forma de uma biblioteca uma API.
    - Method Chain e.g. student.map{...}.filter{...}.distinct()... - reciever
    - Nested Functions distinct(map(filter, student -> ...), ->) - argumento explicito
    
**DSL (Domain Specific Language)** 
    - e.g. Jetpack Compose - Interna 
    - e.g. SQL - Externa

**GPL (General Purpose Language)** - e.g. Kotlin, Java, etc.

## Kotlin

- O kotlin tem 2 DSL para sequences:
    - Collection Extensions - Eager, processamento horizontal, existe em memória (sobre Iterables)
    - Sequence Extensions - Lazy, processamento vertical, podem ser infinitas, existe no tempo que pode ser infinito (sobre Sequence)

As funções têm o mesmo nome simplemente são aplicadas a tipos diferentes

**Tem 1 data source, N operações intermédias e 1 operação terminal**

### Diferenças entre Collection API e Sequence API

- o sequence como é lazy, caso não haja op. terminal não consume 
- O collection como é eager consome sempre.

**Processamento vertical vs horizontal**

Eager - 1 op => todos os elementos
Lazy - 1 elemento => todas as operações
n ops lazy <= ops eager
não implica mais rapidez

**yield** 
```kotlin
fun sequence(block: suspend SequenceScope<T>.() -> Unit): Sequence<T>

fun foo(): Sequence<Int> = sequence {
    // Podem existir pontos de suspensão
    // Gerador
    yield(7)
    yield(6)
    yield(5)
}

val seq = foo()
val iter = seq.iterator()
val item1 = iter.next() // 7, neste momento entra no gerador e executa até ao yield para retornar o valor
```
**TPC** - implementar uma operação concat e concatLazy()

## Posters 

- LAZY, se n existe op terminal -> 0 processamento
- EAGER, PROCESSA SEMPRE
- Estudar como implementar uma operação destas, lazy