# Testes 

- O que acontece quando um assert falha? 
  - por uma exceção, e o teste falha.
- se fizermos asserts em código dentro de threads, as exceções não são observadas e o teste passa.
- Ou não fazer assert dentro da thread
- Ou ter um contentor de exceções thread-safe, e quando a thread termina, verificar se o contentor está vazio, e se não estiver, lançar a exceção.

- Os nossos testes devem ser mais exaustivos. 
- usar ciclos para testar mais casos.
- haver reemparilhamento das threads, com multiplas threads a usar simultaneamente o recurso.
- Limitar o tempo limite do teste ou número máximo de iterações.
- guardar o nr da thread e o nr da iteração, para cada thread fazer exchange do par (t, r)
- verificar a igualdade das listas de pares (t, r) de cada thread.
- neste caso podemos fazer o assert no fim 


# Ex 2

- podemos ter mensagens sem threads bloqueadas
- quando um produtor chega, tem de ver se existem consumidores à espera, porque pode criar condições para ele avançar
- quando o consumidor chega também pode criar condições para os produtores avançarem
