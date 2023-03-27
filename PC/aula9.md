# Aula 9 - Semaphore N-ário

## Semaphore N-ário
> No Exchanger o pedido foi satisfeito com o que foi pedido, tendo informação que caracteriza o resultado do pedido.
> Usar nullable para representar o resultado do pedido.

- Aquisição atómica - pedem N, ou recebem N ou nenhum, se não houver unidades disponíveis aguardam.
- Fairness mesmo que o número de únidades pedido seja maior do que aquele que já existe - FIFO absoluto
- Kernel style
- A desistência de uma thread pode criar condições para outra thread avançar

## Kernel style

- temos 1 ou mais filas que representam pedidos pendentes.
- Os items da fila são representações de pedidos.
- Um item tem uma Condition
  - Para a thread que realizar o pedido poder esperar
  - Sinalizada pela thread que conclui o pedido
  - Forma de saber se o pedido foi concluido
- Se o pedido foi concluído, nunca está na fila.
- No fast path não é necessário aceder à fila.
- A desistência pode acontecer por interrupção ou timeout.
- QUEM FICA RESPONSÁVEL POR COMPLETAR O PEDIDO NÃO É ESTA THREAD (NO KERNEL-STYLE TEM DE SER ASSIM)

```kotlin
private class NArySemaphore(
    val initialUnits: Int,
) {

    private class Request(
        val requestedUnits: Int,
        val condition: Condition,
        val isDone: Boolean = false,
    )
    
    private var availableUnits: Int = initialUnits
    private val requestQueue = NodeLinkedList<Request>()
    private val lock = ReentrantLock()

    fun acquire(requestedUnits: Int, timeout: Duration): Boolean {
        require(requestedUnits > 0) { "requestedUnits must be positive"}
        lock.withLock {
            //fast-path
            if(requestQueue.empty && availableUnits >= requestedUnits) {
                availableUnits -= requestedUnits
                return true
            }

            //slow-path
            val myRequestNode = requestQueue.enqueue(
                Request(requestedUnits, lock.newCondition())
            )
            var remainingNanos = timeout.inWholeNanoSeconds
            while(true) {
                try {
                    remainingNanos = myRequestNode.value.condition.awaitNanos(remainingNanos)
                } catch(e: InterruptedException) {
                    if(myRequestNode.value.isDone) {
                        Thread.currentThread().interrupt()
                        return true
                    }
                    requestQueue.remove(myRequestNode)
                    completeAllThatCanBeCompleted()
                    throw e
                }

                remainingNanos = request.condition.awaitNanos(remainingNanos)
                if(myRequestNode.value.isDone) {
                    return true
                }
                if(remainingNanos <= 0) {
                    requestQueue.remove(myRequestNode)
                    val headRequest = requestQueue.headNode
                    completeAllThatCanBeCompleted()
                    return false
                }
            }
        }
    }

    fun release(releasedUnits: Int) {
        require(releasedUnits > 0) { "releasedUnits must be positive"}
        lock.withLock {
            availableUnits += releasedUnits
            completeAllThatCanBeCompleted()
        }
    }
    
    private fun completeAllThatCanBeCompleted(){
            var headRequest = requestQueue.headNode
            while(!requestQueue.empty && availableUnits >= requestQueue.first.value.requestedUnits) {
                availableUnits -= headRequest.value.requestedUnits
                headRequesst.value.isDone = true
                headRequest.value.condition.signal()
                requestQueue.remove(headRequest)
                headRequest = requestQueue.headNode
            }
    }
}
```

## Thread Pool

### Motivação

- `Pool` é um conjunto de algo que queremos reutilizar ao longo do tempo
- A criação de threads é cara, demora tempo.
- Poupar o tempo de criação de threads.
- Limitar o número de threads existentes, impor um limite de recursos.

### Thread Pool

- podemos definir um número máximo de threads que podem existir
- podemos definir um número mínimo de threads que podem existir
- podemos definir um TTL para as threads