# Aula 2 - Escalonamentos

## Escalonamento

- A ordem das instruções do escalonamento é relevante.
- Num escalonamento é **proibído** trocar a ordem de duas operações dentro de uma transação.
- Duas operações num **escalonamento S** conflituam se se verificarem, **simultâneamente**, as seguintes condições:
    1. As operações pertencerem a transações diferentes.
    2. Ambas as operações acedem ao mesmo item de dados
    3. Pelo menos uma das operações é uma operação de escrita.
- Um escalonamento S é **recuperável** se não existir nenhuma transação que faça
**commit** tendo lido um item depois de ele ter sido escrito por outra transação ainda
não terminada com commit. 

### Propriedades de escalonamentos e problemas

- Se um escalonamento é **_cascadeless_**, não contem **_dirty reads_**.
  - Chama-se **_cascadeless_** porque para contornar este problema deveriamos abortar as transações que leram o valor em cascata.
- Se um escalonamento é **_non cascadeless_**, faz **_dirty reads_**.
- Um escalonamento pode ser não recuperável, por exemplo caso seja **_non cascadeless_** e faça commit antes da transação que depende.
- Um **escalonamento** S diz-se **estrito** se nenhuma das suas transações ler nem escrever um item escrito por outra transação ainda não terminada, ou seja, se não existirem **_dirty reads_** nem **_dirty writes_**(esmagamento de updates não confirmados).
- Um **escalonamento** S diz-se **série** se para toda a sua transação T as operações de T são executadas consecutivamente, sem interposição de operações de outras transações. Limitam a concorrência. Para **N** transações, existem **N!** escalonamentos série possíveis.

- São equivalentes do ponto de vista de conflito se a ordem de quais duas operações conflituantes for a mesma nod dois escalonamentos.
- Escalonamento **serializável (do ponto de vista de conflito)** é quando o mesmo é equivalente em termos de conflitos com um escalonamento em **série**, produz o mesmo resultado que os escalonamentos série.

#### Como abordar exercícios de identificar equivalencias

- Primeiro identificar os conflitos.
- Garantir que a ordem de execução das instruções de conflito se mantém igual à da Serie.
- Garantir a ordem de execução das instruções dentro de cada transação.