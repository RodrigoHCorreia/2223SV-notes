# Aula 6 - Contadores Multi-Thread; Sincronização com locks; Sincronização de controlo

## Continuação do EchoServer

- Temos uma instância da classe que tem lá dentro N threads
- `nOfAcceptedConnections` - contador de conexões aceites, incrementado sempre que uma nova conexão é aceite mas SÓ na main thread (SÓ EM UMA THREAD)
- `nOfActiveConnections` - contador de conexões ativas, incrementado em qualquer thread 
- `nOfHandledMessages` - contador de mensagens tratadas, incrementado em qualquer thread 
- As duas últimas são Dados a serem alterados por mais que uma thread, logo temos de ter cuidado com a sincronização, porque pode haver esmagamento de dados.
- Este problema resolve-se com um simples atomic Integer, porém isto só funcionaria neste caso em particular por ser um Integer.
- A forma mais genérica de resolver este problema é com locks (Exclusão Mútua):
  - `ReentrantLock` - ao adquirir o lock, a `thread` que o adquiriu pode adquirir o lock novamente, **e só o liberta quando o número de locks for igual ao número de unlocks.**
  - o problema em adquirir o lock, afetar e libertar o lock, é que caso haja uma exceção dentro do bloco, o lock não é libertado, logo temos de usar o try-finally, ou o `withLock` que faz isto por nós.
  - **A JVM não tem qualquer tipo de deteção de deadLock**, (ao contrário do SGBD) por isso temos de ter cuidado com isto.

- Como nos proteger contra deadlocks:
  - **Usar sempre que possível só um lock**
  - **Não fazer operações bloqueantes na posse do lock**
```kotlin
class ThreadSafeCounter {
    private var value: Int = 0
    private val theLock: Lock = ReentrantLock() 
    
    fun inc() = theLock.withLock {
            add(1)
        }

    fun read(): Int = theLock.withLock {
            return value
        }

    fun dec() = theLock.withLock {
            add(-1)
        }

    fun add(delta: Int) = theLock.withLock {
            value += delta
        }
}
```

- se quisermos que só processe algo se o contador for menor que N, podemos resolver de duas formas:
  - fazer polling e ir verificando se o contador é menor que N, com um sleep
  - com sincronizador, através de um monitor.

### Monitor

- Sincronização de dados + sincronização de controlo = `Monitor`
  - Para a sincronização de dados, usamos o lock
  - Para a sincronização de controlo, usamos a condição, que está coordenada com o lock.

- `condition.await()` - promove o estado da thread para not-ready, e `liberta o lock` associado à condição, e espera até que se chame signal.
  - o retorno do await só acontece quando a thread readquire o lock e temos garantia que estamos na posse do lock.
  - porém não podemos garantir que a condição que verificamos para dar 
- condition.signal() - acorda uma thread que esteja em estado not-ready, e que esteja à espera de um signal.
  - várias threads podem esperar na mesma condição, e só uma é acordada por cada signal, e **a ordem de acordar é indeterminada**.
  - ao haver perda de exclusão mútua, não garantimos que a exclusão mútua é adquirida pela thread que acordamos, logo não temos garantia que a condição que verificamos para dar return ao await é verdadeira.
- condition.signalAll() - acorda todas as threads que estejam em estado not-ready, e que estejam à espera de um signal dessa condição.
- `"spurious wakeup"` - o await pode retornar sem nenhuma razão, por isso temos de verificar a condição que queremos, e se não for verdadeira, voltar a fazer await.
```kotlin
class TheSynchronizer {

    private val maximumConctions = 2
    private var nOfActiveConnections: Int = 0
    private val theLock: Lock = ReentrantLock()

    private val condition: Condition = theLock.newCondition()

    fun startHandlingConnection() = theLock.withLock {
        nOfActiveConnections += 1
    }

    fun endHandlingConnection() = theLock.withLock{
        nOfActiveConnections -= 1
        if(nOfActiveConnections < maximumConctions) condition.signal()
    }

    fun waitForActiveConnectionsBelowMaximum() = theLock.withLock{
        //fast-path
        if(nOfActiveConnections < maximumConctions) return@withLock

        //wait-path
        while(!(nOfActiveConnections < maximumConctions))){
            condition.await()
        }
    }
}

//Lida também com recurso, porém especifica à cabeça a quantidade disponível e só subtrai a essa quantidade adquirindo recursos ou libertando-os 
class AnotherSynchronizer( initialAvailableResource: Int) {

    private var availableResource: Int = initialAvailableResource
    private val lock: Lock = ReentrantLock()
    private val condition: Condition = lock.newCondition()

    fun acquireResource() = lock.withLock{
        while(availableResource == 0){
            condition.await()
        }
        availableResource -= 1
    }

    fun releaseResource() = lock.withLock{
        availableResource += 1
        condition.signal()
    }

}
```

### AnotherSynchronizer / Semaphore

- agora o acquire pergunta se o recurso está disponível e **adquire** o recurso caso disponível
- este sincronizador chama-se `Semaphore`.
- onde os resources se chamam `permits/units`
- podemos também fazer acquire de N resources, e release de N resources.
