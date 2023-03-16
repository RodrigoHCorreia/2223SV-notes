# Aula 6 - Transações com 2PL EM PGSql

- nos updates o PGSql faz coisas diferentes.
- no PGSql só se acede a coisas que estejam estáveis no momento em que a transação acabou e não são atrasadas.
- Existe um mecanismo de `snapshot isolation` que permite que as transações vejam o estado do sistema no momento em que a transação começou.