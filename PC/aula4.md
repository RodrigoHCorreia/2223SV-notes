# Aula 4 Threads in C with Join; Threads in Kotlin

## Threads in C

- **ut_join(uthread_t *thread)** 
  - o descritor da thread não deve ser apagado após a thread terminar, porque a thread pode ser chamada pelo join, ou ser perguntada se já terminou. 
  - então fazemos dois mallocs, um para o descritor da thread, e outro para o stack da thread.
  - logo podemos verificar se a thread já terminou se o stack for NULL.
  - caso não tenhamos condições para avançar, acrescentamos a thread à lista de joiners da thread que está a correr.

- **internal_start()** 
  - chama-se o entry-point da thread
  - após isso, enquanto a lista de joiner não estiver vazia, vamos passando as threads à lista de ready, e depois chamamos o scheduler e damos free ao stack da thread.

## Threads in Kotlin

- AtomicInteger - classe que permite incrementar e decrementar um valor de forma atómica, por multiplos threads.
- incrementAndGet() - incrementa o valor e retorna o valor incrementado.
- map - thread-safe.
- porém mesmo o map sendo atómico e o AtomicInteger sendo atómico, não podemos garantir que o incremento e o decremento são atómicos, porque o algoritmo não é atómico.
- Check then Act - verificar se a condição é verdadeira, e depois agir, mas pode ser que entre noutro thread e mude o valor.
- Este problema resolve-se com sincronização adequada.

## Sincronização adequada

- ConcurrentHashMap 
  - computeIfAbsent - se a chave não existir, cria-se um novo valor, e se existir, retorna-se o valor existente.
- Lock - garante Exclusão Mútua.
  - lock() - bloqueia o lock.
  - unlock() - desbloqueia o lock.
- ReentrantLock - permite que um thread possa bloquear o lock várias vezes, e só o desbloqueia quando o número de locks for igual ao número de unlocks.