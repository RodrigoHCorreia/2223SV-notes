# JMM 

## Java Memory Model (JMM)

```kotlin

Class A {
    val field = "Hello"
}
var r: A? = null

T1:
r = A()

T2:
val observer = r
if(observer != null) {
    println(observer.field)
} else {
    println("still null")
}
```
- **O que pode aparecer no standard output**
a) "Still null"
b) "Hello"
c) não há garantias que a leitura lê hello, podendo então a leitura ao field pode ser nula.
(pelo facto de field ser um val e não um var, o modelo de java até pode garantir que a leitura lê hello)

- enquanto o modelo não tiver garantias, não podemos assumir que o modelo é sequencial.


```kotlin
Class A {
    var field = "Hello"
}
val q = BlockingQueue<A>()

T1:
val r = A()
q.add(r)

T2:
val observer = q.take()
println(observer.field)
```

- **O que pode aparecer no standard output**
- a) "Hello"

### BlockingQueue

- thread safe
- Memory consistency effect:
  - Ações numa thread antes de colocar um object na queue, HB ações subsequentes de acesso ou remoção desse objecto da queue.
- faz um read bloqueante, logo o q.take() garante que a thread 2 vai ver o write da thread 1.

- A JMM
- Ñao garante a consistencia sequencial
- Relacao HB
- garantias dadas pela relacao HB
- Regras construcao da relacao HB
-Se uma ação de leitura volatile r ler o valor escrito por uma ação de escrita volatile w, então w -> r

escritas e leituras *volatile*
em java existe a palavra reservada em kotlin annotation

- **ler o capitulo 16/15 do livro**

- todas as escritas e leituras em atomicos sao volatile
- **volatile não garante atomicidade**

### Compare and set

```kotlin

class ThreadSafeCounter {

    private val value = AtomicInteger()

    fun inc(): Unit { 
        value.incrementAndGet()
    }

    fun dec(): Unit { 
        value.decrementAndGet() 
    }

    fun get(): Int { 
        return value.get() 
    }
}

class ThreadSafeModuloCounter(
    private val modulo: Int
){

    init {
        require(modulo > 0)
    }

    private val value = AtomicInteger()

    fun inc(): Unit {
        while(true) {
            val observed = value.get()
            val next = if(observed < modulo - 1){
                observed + 1
            } else {
                0
            }
            if(value.compareAndSet(observed, next)){
                return
            }
        }
    }

    fun dec(): Unit {
        while(true) {
            val observed = value.get()
            val next = if(observed > 0){
                observed - 1
            } else {
                modulo - 1
            }
            if(value.compareAndSet(observed, next)){
                return
            }
        }
    }
}
```

```kotlin
class TreiberStack<T> {

    private class Node<T>(
        val value: T,
        var next: Node<T>? = null
    ) 
    

    private val head = AtomicReference<Node<T>?>(null)

    fun push(value: T): Unit {
        val newHead = Node(value)
        while(true) {
            val observedHead = head.get()
            newHead.next = observedHead
            if(head.compareAndSet(observedHead, newHead)){
                return
            }
        }
    }
    //Why can't the newHead.next = observedHead be done after the CAS?
    //
}
```
## Posters

- O VOLATILE N GARANTE ATOMICIDADE
  - um volatile integer não é atomico