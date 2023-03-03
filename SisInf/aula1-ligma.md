# Aula 1 - Sistemas de Informação (Made by @Ligma)
> Faltei.

### Dúvidas

### Aulas

- Aula Prática no final do 1º módulo
- Aula Prática no final do 2º módulo
- Sessões práticas ao longo do 3º Módulo

### Trabalhos
- Trabalho com 2 fases
    - 1º fase associada ao módulos 1 e 2 (Treinar transações e SQL programático)
    - 2ª fase associada à utilização do JPA (e possivelmente outros modelos?)
- Este professor não vai deixar entregar trabalhos mais tarde
   
### Avaliação

- Exame
- Não existem testes parciais
- Nota Final = 50% Prática + 50% Teórica

### Bibliografia
- Fundamental of Database Systems
- Inferência específica dos fabricantes

### Setup de trabalho

- PostgreSQL
- SGBD
    - pgAdmin4
    - dbeaver
    - DataGrip
- JPA
    - Eclipse IDE
    - IntelliJ
    - vim (vou fazer a cadeira com este)

## Processamento Transacional

- Transações porquẽ?
    - Recuperação
    - Anular processamento anterior      
    - Controlar a interferência 

Controlar a interferência não é sempre crítico ou necessário de ser imediato. Por exemplo: um catálogo mostrar um produto já inexistente/sem stock.

Ação atómica: Aquela que, quando executada num determinado nível de abstração, ou é executada completamente com sucesso. Uma ação pode ser atómica num nível de abstração mais elevado, mas não num nível de abstração mais baixo.

Ações:
- Não protegidas - efeitos necessitam ser anulados (ex. ficheiro temporário)
- Protegidas - efeitos necessitam de ser anulados se algo falhar (ex. transações)
- Reais - objetos físicos. Não pode ser anulado (ex. lançar um míssil)

### Transações

Fornecer mecanismos de recuperaçãio em caso de falhas do sistema.
Facilitar o tratamento de erros ao nível das aplicações

**Propriedades ACID:**
  * Atomicity (Atómica)
  * Consistency preservation (Consistência)
  * Isolation (Isolamento)
  * Durability (Durabilidade)

### Escalonamento
Um escalonamento (história) de um conjunto de transações {T1, T2, ..., Tn} é a ordenação S das operações de cada Transação (Ti).
