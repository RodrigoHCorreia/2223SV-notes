# Exercícios

## ER

### Ex 1

```kotlin
class ExecutorWithShutdown(private val executor: Executor) {

    private enum class State { RUNNING, TERMINATING, CLOSED }

    private var state = State.RUNNING
    private val lock = ReentrantLock()
    private val runnableQueue = LinkedList<Runnable>()
    private var current: Runnable? = null

    val terminationList = LinkedList<Waiter>()

    private class Waiter(val condition: Condition){
        var shutdown = false
    }

    private fun alertTerminationWaiters() {
        terminationList.forEach{
            it.shutdown= true
            it.condition.signal()
        }
    }

    private fun fetchNext() {
        monitor.withLock{
            if(!runnableQueue.isEmpty()) {
                current = runnableQueue.poll()
            }
        }
        if(current != null) executor.execute(current!!)
    }

    private fun runWithoutException(runnable: Runnable) : Runnable {
        return Runnable {
            try{
                runnable.run()
            } finally {
                fetchNext()
            }
        }
    }

    @Throws(RejectedExecutionException::class)
    fun execute(command: Runnable): Unit {
        lock.withLock{
        if(state == State.CLOSED) 
            throw RejectedExecutionException()

        runnableQueue.add(runWithoutException(command))
        current?: fetchNext()
        }
    }
    
    fun shutdown(): Unit {
        lock.withLock{
            if(state == State.RUNNING) {
                if(runnableQueue.isEmpty()){
                    state = State.CLOSED
                    alertTerminationWaiters()
                } else{
                    state = State.TERMINATING
                    while(runnableQueue.isNotEmpty());
                    state = State.CLOSED
                    alertTerminationWaiters()
                }
            }
        }
    }
    
    @Throws(InterruptedException::class)
    fun awaitTermination(timeout: Duration): Boolean {
        lock.withLock{
            if(state == State.CLOSED) return true
            
            val waiter = Waiter(lock.newCondition())
            terminationList.add(waiter)
            val remainingNanos = timeout.inWholeNanoseconds
            while(true){
                try{
                    remainingNanos = waiter.condition.awaitNanos(remainingNanos)
                } catch(e: InterruptedException){
                    if(waiter.shutdown){
                        Thread.currentThread().interrupt()
                        return true
                    }
                    terminationList.remove(waiter)
                    throw e
                }

                if(waiter.shutdown) return true

                if(remainingNanos <=0) return false
            }
        }
    }
}
```

### Safe Container

```kotlin
private class UnsafeValue<T>(val value: T, var lives: Int)
private class UnsafeContainer<T>(private val values: Array<UnsafeValue<T>>){
    private var index = 0
    fun consume(): T? {
        while(index < values.size) {
            if (values[index].lives > 0) {
                values[index].lives -= 1
                return values[index].value
            }
            index += 1
        }
        return null
    }
}

private class Value<T>(val value: T, var lives: Int)
private class Container<T>(private val values: Array<Value<T>>){
    private var index = AtomicInteger(0)
    private var values = Array(values.size) { AtomicReference(values[it]) }
    fun consume(): T? {
        while(true){
            val obsIdx = index.get()
            if(obsIdx >= values.size) return null
            val reference = values[obsIdx]
            val curr = reference.get()
            if(curr.lives > 0){
                if(reference.compareAndSet(curr, Value(curr.value, curr.lives - 1)))
                    continue
                return curr.value
            }
            while(!(index.compareAndSet(obsIdx, obsIdx + 1)));

        }
    }
}
```

```kotlin
class Exchanger<T> {
    private class Trader<T>(val continuation: Continuation<T>, val message: T)
    private var waiterTrader : Trader<T>? = null 
    private var secondTrader : Trader<T>? = null
    private val lock = ReentrantLock()
    suspend fun exchange(value: T): T = suspendCoroutine { 
        lock.withLock { 
            if(waiterTrader == null)
                waiterTrader = Trader(it, value)
            waiterTrader.continuation?.resume(value)
            return waiterTrader.message
        }
    }
}
```