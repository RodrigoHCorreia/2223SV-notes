## Aula 3 - Threads em baixo nível; Estados da thread

```assembly
context_switch:

pushq %rbx
pushq %rbp 
pushq %r12
...
pushq %r15
movq %sp, (%rdi) # guardar o endereço da próxima instrução da thread chamadora
# Mudança de contexto
mov (%rsi), %sp # report o endereço da próxima instrução da thread chamada
popq %r15
...
popq %r12
popq %rbp
popq %rbx
ret
```

- realizar um context switch da th0 para a th1 é guardar o estado da th0 e carregar o estado da th1, cada uma no seu respetivo stack.
- O stack pointer é a coisa mais importante que determina o contexto da thread, e o descriptor de cada thread, contém o stack pointer, e mais algumas coisas a ver futuramente.
- na criação da thread, inicia-se o stack a fazer de conta que se fez um context switch, e cria-se o descritor da thread.

### Thread States and concepts

- **running** - a thread está a ser executada em um CPU. 
- **ready** - a thread está pronta para ser executada, mas não está a ser executada em nenhum CPU.
- **not ready** - a condição necessária para avançar não é verdadeira, e não está a ser executada em nenhum CPU.
- **not started** - a thread ainda não foi started.

- **Quem determina se a condição necessária é verdadeira ou não é o SO** e não a thread, logo o SO irá chamar de novo a thread quando a condição se verificar, **logo a thread não gasta processador**.

- **Cooperativo** - as threads só perdem processador por ação da thread.
- **Preemptivo** - o Scheduler pode tirar uma thread de execução mesmo que sem a thread querer.

### C Threads

- **uthread_t** - estrutura que representa uma thread
- 