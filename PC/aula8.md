# Aula 8 - Interrupção de threads, Kernel style

## Interrupção de threads

- mecanismo de cancelamento de threads da JVM
- A T1 pode fazer set à flag de interrupção à flag da T2
- a T2 pode observar o seu estado de interrupção e afeta-lo.
- para **algumas** chamadas potêncialmente bloqueantes: (ex. cond.await(), sem.acquire())
  - se a flag estiver ativa na chamada dessa função, a mesma termina imediatamente com o lançamento de **InterruptedException**
  - se a flag não estiver ativa a operação fica bloqueada, mas se durante o tempo de bloqueio a flag for ativada, a operação termina na mesma com o lançamento de **InterruptedException**.
  - ao lançar a exceção a flag é desativada automaticamente.
- o mecanismo de interrupções **PEDE** para a thread acabar, não termina a thread nem mata a thread.
- **TODAS** as funções potencialmente bloqueantes que fizermos devem ser sensíveis a interrupções.

## SemaphoreWithFairnessAndSpecificSignaling

- Fila de pedidos - requestQueue
- é criada uma condição para o pedido e põe-se na fila.
- só sinalizar quando o pedido for o primeiro da fila.

## ManualResetEvent 

- não se adquire unidades, espera-se que seja true, quando está true as threads em espera podem seguir

```kotlin
Class ManualResetEvent {

    private var value: Boolean
    private val lock : ReentrantLock
    private val condition = lock.newCondition()

    fun set() = lock.withLock {
        value = true
        condition.signalAll()
    }

    fun reset() = lock.withLock {
        value = false
    }

    @Throws(InterruptedException::class)
    fun waitUntilSet(timeout: Duration): Boolean { 
        lock.withLock {
        //fast-path
        if(value) return true

        //slow-path
        var remainingNanos = timeout.inWholeNanoSeconds
        while(true) {
            remaining nanos = condition.awaitNanos(remainingNanos)
            if(value) return true
            if(remainingNanos <= 0) return false
        }

    }
}
```

## ManualResetEventKernelStyle


```kotlin
Class ManualResetEventKernelStyle {

    

    private class Request(
        val condition: Condition,
        val isDone: Boolean = false,
    )

    private var value: Boolean
    private val lock : ReentrantLock
    private var currentResquest = Request(condition = lock.newCondition())
    fun set() = lock.withLock {
        value = true
        currentRequest.condition.signalAll()
        currentRequest.isDone = true
        currentRequest = Request(condition = lock.newCondition())
    }

    fun reset() = lock.withLock {
        value = false
    }

    @Throws(InterruptedException::class)
    fun waitUntilSet(timeout: Duration): Boolean { 
        lock.withLock {
        //fast-path
        if(value) return true

        //slow-path
        var remainingNanos = timeout.inWholeNanoSeconds
        val localRequest = currentRequest
        while(true) {
            remaining nanos = localRequest.condition.awaitNanos(remainingNanos)
            if(localRequest.isDone) return true
            if(remainingNanos <= 0) return false
        }

    }
}
```

- temos a certeza que o 

### Duvidas Série 1

pergunta 1 - exchanger
Exchanger é um sincronizador que quando uma thread entrega um valor b1 e outra thread entrega um valor b2, a primeira thread vai com b2 e a segunda com b1, ver a biblioteca standard de classes.
(entrega == chamar o método)

- Exchanger de dimensão N 
- Exemplo de grupo de dimensão 3 com 4 threads a chamar
- chamada do exchanger são **potencialmente bloqueantes** recebendo um timeout. a quarta chamada fica à espera que se reunam condições para ser formada grupo, se não retorna **null**
- **1 chamada só pode participar em 1 grupo**
- se C1 recebe uma lista com o valor da chamda C2 
- então C2 tem de receber uma lista com o valor da chamada C1
- `Monitor style` - Solução com monitor, com condição, com lock, com variáveis de condição
- **O Primeiro problema tem uma solução simples com kernel-style**