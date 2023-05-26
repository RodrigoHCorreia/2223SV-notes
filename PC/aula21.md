# Aula 21

Continuation Passing Style - passa-se a função de continuação como argumento para a função que a chama, caso ela tenha parâmetros, os mesmos são passados como argumentos para a função de continuação.

fun f(i: Int): String -> Direct Style

fun f(i: Int, continuation: Continuation<String>): Any -> Continuation Passin Style

Uma função suspend, escrita em direct style no código fonte mas usa CPS no bytecode.

Caso o resume seja chamado dentro da suspendCoroutine onde é criada a continuation, é dito que a continuação é executada de forma síncrona, caso o resume seja chamado fora da suspendCoroutine onde é criada a continuation, é dito que a continuação é executada de forma assíncrona.

Existem duas maneiras de retorno de uma função que chama uma continuation, e por isso é que em kotlin a mesma retorna `Any`:
    - Ou a função retorna um valor e a continuation é chamada com esse valor
    - A função retorna uma marca especial que serve para indicar que a corrotina ainda não produziu um valor.

Em kotlin o resume só pode ser chamado uma vez, pois estas são chamadas de "Single Shot Continuations", caso seja chamado mais que uma vez, é lançada uma `Exceção`.

É o dispatcher que executa a continuação para ter a garantia que a continuação se executa no dispatcher correto. Se o dispatcher for Unconfined a continuação é executada na thread que chamou o resume, caso contrário é executada no dispatcher.

- **A função do dispatcher é promover a execução.**



```kotlin
val res = suspendCoroutine { Continuation ->

    Continuation.resume("Hello")
    //Código

}

println(res)
```

## Todo: Ver a aula 20 no git da cadeira.

SuspendableCountDownLatch - é um CountDownLatch que pode ser usado em corrotinas.

```kotlin

// Espera suspende corrotinas sem bloquear threads, logo await tem de ser suspend
class SuspendableCountDownLatch(
    initialCount: Int
) {
    private var count: Int = initialCount
    private var continuationList = mutableListOf<Continuation<Unit>>()
    private val lock = ReentrantLock()

    fun countDown(): Unit {
        var continuationListToResume = mutableListOf<Continuation<Unit>>()
        lock.withLock {
            count -= 1
            if(count == 0){
                countinuationList.forEach { it.resume(Unit) }
            }
        }
    
    }

    suspend fun await(): Unit {
        if(count== 0) return
    
        suspendCoroutine<Unit> { continuation ->
            continuationList.add(continuation)
        }
    }
}
```



## POSTERS

**CORROTINAS SUSPENDEM-SE, THREADS BLOQUEIAM-SE**