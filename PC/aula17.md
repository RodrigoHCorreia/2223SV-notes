# Futures 

- Interface síncronas e interface assíncronas para operações
- Future enquanto representante da operação ou do resultado da operação que está a ser realizada de forma assíncrona.

- Duas interfaces
- Interface Future<T>
  - "Polling"
  - Bloquear até operação estar concluída
- Interface CompletionStage<T>
  - Combinação
    - CS<T>, T->R -> CS<R>
  - Classe concreta CompletableFuture<T>
    - Implementa ambas as interfaces

### CompletionStage<T>

- Criação de pipelines de processamento, usando os combinadores
  - Assembly phase
  - Data-flow phase

Os callbacks são executados por omissão na thread que completou o future.