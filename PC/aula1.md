# Aula 2 - Programação Concorrente
> Faltei a aula, apontamentos rudimentares e incompletos

### Dúvidas

- Ver se nos apontamentos faltou algum ponto chave ou se há alguma incoerência.
- Porque fazer o join a seguir ao start para garantir o sincronismo? não chega o join?
- O sentido de ao variar o número de reps o teste poder falhar tem haver com o facto do assert ser executado antes da thread terminar?
- "Segundo" EchoServer comecei a ouvir pior e não entendi bem a utilização do clientId e tenho a impressão que não ouvi bem a intenção do mesmo.
- Verificar reader e writer se é o que eu entendi.
---
* Alterar thread para Thread, é um construtor e não uma função.

## Pontos que consegui apanhar

### Criação de um EchoServer
> necessário as bibliotecas java.net e java.io, e a app **netcat** para testar o servidor.

- **bind(InetSocketAddress)** - associa o socket a um endereço, pois o socket é criado sem um endereço associado.
- O servidor não se liga a ninguem, o **cliente** é que **toma a iniciativa** de se ligar ao servidor.
- Um **socket** é um endpoint de comunicação, ou seja, **é um par de endereços (IP e porta)**, usado como uma interface para o protocólo **TCP/IP**.
- o **inputStream** fornece um canal de leitura de dados em bytes.
- o **outputStream** fornece um canal de escrita de dados em bytes.
- o **reader** (lê sequências de caracteres) transforma uma sequência de bytes numa sequência de caracteres. **??**
- o **writer** (escreve sequências de caracteres)transforma uma sequência de caracteres em uma sequência de bytes. **??**
- um **runnable** é um objeto que tem um método run, sem parametros e sem retorno.
- **Thread** { ... } cria uma thread que executa o código dentro das chaves, mas como é um trailling lambda, não é necessário passar o runnable como argumento.
  

```kotlin   
private fun main(){
    EchoServer().run()
}

class EchoServer {
    private val serverSocket = ServerSocket()

    fun run() {
        serverSocket.bind(InetSocketAddress(8080))
        val th = thread {
            while (true) {
                val socket = serverSocket.accept()
                val inputStream = socket.getInputStream()
                val outputStream = socket.getOutputStream()
                val reader = inputStream.bufferedReader(
                val writer = outputStream.bufferedWriter()
                var lineNumber = 0
                while (true) {
                    val line = reader.readLine() ?: break
                    val response = "$lineNumber: ${line.toUpperCase()}"
                    writer.writeLine(response)
                    lineNumber++
                    writer.flush()
                }
                socket.close()
            }
        }
        th.start()
    }
}
```

#### Corrotinas em Kotlin

- as **corrotinas** são uma forma de ter multiplas computações ao mesmo tempo. Têm suporte para **concorrência estruturada**, logo acaba por ser mais "organizado" .
- quando criamos uma thread, criamos uma thread no contexto em que a thread criadora está a correr, porém é independente da thread criadora.

### Testing Threads

```kotlin
class ThreadBasicsTest {

    private var anInteger = 0

    @Test
    fun first() {
        val th = thread {
            anInteger +=1
        }
        th.start()
        assertEquals(1, anInteger)
    }
}
```	

- **Não temos garantias** de que a thread vai terminar antes do teste acabar, logo, temos que esperar que a thread termine antes de fazer o assert, caso contrário, **o assert pode falhar.**
- **O Comportamento deste teste pode variar de execução para execução.**
- **Se quisessemos garantir** que a thread termina antes do teste acabar, poderiamos usar o método **join().**
- Podemos coordenar(**SINCRONIZAR**) a execução das threads de modo a ter alguma forma de garantir que a thread termina antes do teste acabar.
- O metodo **join()** fica à espera que a thread termine, e só depois continua a execução.
- Quando o join retorna, **tudo aquilo que a thread fez sobre a memória, já foi realizado**, logo, **o assert não vai falhar.**

```kotlin
    @Test
    fun second() {
        val N_OF_THREADS = 10
        val N_OF_REPS = 1000 // conforme o número de reps pode falhar ou não
        val ths = mutableListOf<Thread>()
        repeat(N_OF_THREADS) {
            val th = Thread {
                repeat(N_OF_REPS) {
                    anInteger += 1
                }
            }
            th.start()
            ths.add(th)
        }
        ths.forEach {
            it.join() //PORQUE?? NÃO ENTENDI
        }
        assertEquals(N_OF_THREADS * N_OF_REPS, anInteger)
    }
}
```
- Conforme o número de repetições aumenta, o teste pode falhar, pois o assert pode ser executado antes das threads terminar.
-  **acesso por multiplas threads a dados mutáveis está por omissão errado(produz comportamento não determinista.**
---

#### EchoServer V2

```kotlin 
package sketches.temp.leic41d

import java.net.InetSocketAddress
import java.net.ServerSocket

private fun main() {
    EchoServer().run() // previous error was the missing parentheses in EchoServer
}

class Counter {
    private var value = 0

    fun inc(): Int {
        value += 1
        return value
    }
}


class EchoServer {

    fun run() {
        val serverSocket = ServerSocket()
        serverSocket.bind(InetSocketAddress(8080))
        val clientId = Counter()
        while (true) {
            val clientSocket = serverSocket.accept()
            val reader = clientSocket.getInputStream().bufferedReader()
            val writer = clientSocket.getOutputStream().bufferedWriter()
            val newClientId = clientId.inc()
            val th = Thread {
                var lineNumber = 0
                writer.writeLine( "Welcome client ${newClientId.inc()}")
                while (true) {
                    val requestLine: String = reader.readLine() ?: break
                    val response = "$lineNumber:${requestLine.uppercase()}"
                    lineNumber += 1
                    writer.writeLine(response)
                }
            }
            th.start()
        }
    }
}
```
- Temos duas ou mais threads, sem garantia de sincronização, logo, **o comportamento do programa pode variar de execução para execução.**
- Este problema pode ser resolvido com atomicIntiger, que garante que o incremento é feito de forma atómica, ou seja, **não há interrupções durante o incremento.**
- Também poderia ser resolvido com o uso de locks.
- Ao mover o incremento para a thread principal é uma forma de evitar a partilha de dados entre threads, logo, não há necessidade de sincronização.

