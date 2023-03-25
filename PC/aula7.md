# Aula 7 - Semaphore com timeout, Semaphore with fairness
Apontamentos a tirar, não pude ir à aula, continuação de semaphore

## Semaphore with timeout

- garantir probabilidades de `liveliness`, se existem unidades não podemos ter uma thread sempre à espera dessa unidade.
  - garantir que as sinalizações são sempre feitas e nunca se perdem.
  - as sinalizações podem-se perder quando à saida do await, ter cuidado com a ordem, **verificar primeiro se existem unidades e depois verificar o timeout**.
  - a thread que foi alvo do signal pode desistir.

- Desistência e ordem de avaliação da desistência. (com a introdução do timeout)
- isto aumenta a probabilidade de haver erros de liveliness.
- `safety` uma unidade nunca é concebida se não existir.
  - garantido facilmente porque ela só sai quando decrementar o contador após ser concebida

## Semaphore with fairness

- `fila de pedidos`
- a atribuição de unidades agora também tem de respeitar a fila
- um pedido é uma chamada a uma operação potencialmente bloqueante, no caso do semaphore é o pedido do acquire.
- **Manutenção da fila** em caso de saída com sucesso e saída por desistência.
- utiliza-se signalAll() o que é um problema de desempenho.
