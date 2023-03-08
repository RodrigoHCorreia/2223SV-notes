# Aula 2 - Stack, CPU and Memory in the context of concurrency;  

## Key concepts, terms and definitions

- A zona de **_code_** é partilhada entre os processos, pois é read-only, logo não gera problemas de concorrência.
- O **_stack pointer_** é referente ao contexto da computação, logo a zona do stack não é partilhada.
  - Cada computação tem o seu **stack**.
  - memoria read-write, porém não partilhada.
- A zona de **_data_** é read-write e partilhada, logo é onde vão incidir a maior parte dos problemas de concorrência.
- Ao abrir o mesmo programa diferentes vezes criamos diferentes processos com instâncias diferentes, processos esses completamente isulados.
- **Multiplexagem temporal** é o que permite que o computador execute várias tarefas no mesmo CPU.
- **context switch** é a troca de estado de um processo para outro, no mesmo CPU.
- Por omissão, não existe afinidade entre CPUs e threads.
- Um **CPU** só pode executar uma thread por vez.
- Uma **thread** só pode ser executada num CPU por vez.

### Convenção de passagem de argumentos
>Existem várias convenções, porém para duas funções cooperarem é necessário que sejam iguais
- Define o registo do retorno de uma função.
- Define os registos onde são passados os argumentos (até 6 argumentos).
- Define registos que é necessário preservar.