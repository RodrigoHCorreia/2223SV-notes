# Aula 15 - Java Memory Model (JMM)

## Memory Model

- conjunto de características de um sistema que nos permitem responder a questões como:
  - Dada uma ação de leitura sobre uma variável, qual o valor que será lido por essa ação?
    - O valor da **última** ação de escrita sobre essa variável
    - Num sistema com múltiplas threads, não existe "última"

- A maior parte das pessoas assume que o modelo de consistência sequêncial.

## Java Memory Model (JMM)

- **Estranho**
- O modelo de consistência sequêncial não é garantido pela JMM nem pelos modelos de memória multi-processador.
- O que é relevante são as ações.
- Umas das ações pode ser uma leitura ou uma escrita.

- **Ação de leitura**
- **Ação de escrita**

- **ver uma ação** - 
- relação de ordem parcial -> **happens-before** (HB)

- uma relação é um produto cartesiano entre conjuntos (ações)
- uma relação é um conjunto de pares ordenados (ações)

- **Exemplo**
dado o conjunto A = {0, 1, 2}
uma R é o conjunto do produto cartesiano de A com A
R = {(0, 1), (1,2) , (0,2), (0,0), (1,1), (2,2), }
Relação de ordem total -> R
Relação de ordem parcial -> se existirem elementos que não são comparáveis.
Se fizermos R - (1,2), o 1 e o 2 não são comparáveis.


- a4(write) = 0x123
- a1(read) = 0x123

- a hb b = (a,b) ∈ HB


#### Garantias:
- para estas garantias não é necessário que seja executados na mesma thread.

- se tiver um par (write, read) sobre a mesma variável, e se estiverem relacionados por HB, e não existirem mais writes sobre essa variável, então o read garantidamente lê o valor escrito pelo write. (write -> read)
- se o par (read, write) estiver na relação, temos a garantia que o read não vê o write. (read -> write)
- se tiver um write 0 -> write 1 -> read, então o read vê o write 1.
- se tivermos write 1 -> read e write 2 não relacionado com nenhum dos dois, não sabemos o que o read vê.
- 
**Como é que se constroi a relação**:
- Se a e b são ações da mesma thread, e a está antes de b, então a -> b. ((a,b) ∈ HB)
- Lock e unlock sobre o mesmo lock, são ações e a ordem HB é total sobre essas ações. ação de lock l e ação de unlock l, então ou lock -> unlock ou unlock -> lock.
- HB é transitiva. Se a -> b e b -> c, então a -> c.
**FALTAM REGRAS**

- T1: write -> terminate
- T2: join -> read
- Existe obrigatóriamente uma relação entre o terminate -> join, porque o join garante que iremos ver as escritas feitas pela thread T1.

- write -> start, 

- **Exemplo**
```
**T1:**
lock
write
unlock

**T2:**

lock
read
unlock
```
