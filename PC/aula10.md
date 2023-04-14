# Aula 10 - Thread Pool

- criar uma thread dentro com a posse da exclusão mutua, significa que a execução dessa thread **não tem exclusão mutua**.

Um callable ou produz um T ou lança uma excepção.
o retorno do execute passa a ser um Future<T> que pode ser usado para obter o resultado ou a excepção.