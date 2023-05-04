# Aula 16 - Monitores implícitos, publicação de objetos, Futures

## Monitores implicitos (intrinsecos)

- Qualquer objecto, i.e, qualquer instância de um tipo que deriva de Object:
    - é um lock
    - é uma condição desse lock 

- Monitores explícitos - interface `Lock` e interface `Condition`
- Aquisição do lock: 
    - Statement synchronized { bloco }
    - Métodos com o modificador @synchronized 
      - Se for **método de instância** o lock associado ao `this` é adquirido automaticamente no inicio e libertado automaticamente no fim
      - Se for um **método estático** é usado o objeto classe
- Classe base Object tem métodos:
    - wait() - espera na condição implicíta
    - notify() e notifyAll() - sinalização da condição implícita

- Todos os conceitos aplicados aos monitores explícitos falados anteriormente aplicam-se aos monitores implícitos exceto que os **implícitos só permitem uma só condição**.


```java
Object l = new Object()

//String s = "Hello" // isto também seria possível pois qualquer objeto pode ser um lock
synchronized(l/*s*/){ 
    bloco
}
```

## Publicação de objetos

- **publicação de objetos** - criar um objeto numa thread e usar esse objeto noutra thread.

- isto só é possível se o objeto for corretamente publicado 

**HB garantees trough**: 
    - referência volatile
    - thread start

- campos final em java temos garantia de que o objeto é corretamente publicado. (o mesmo se aplica para o val do kotlin).
- o campo final garante que o seu acesso através de uma referência bem construida, garante o seu valor inicial.

## Future

- Representa (o resultado de) uma operação/computação, potencialmente em curso.

**operação sincrona** -> a função retorna quando a operação acabou. o retorno está síncrono com a terminação da operação.

**operação assincrona** -> o retorno não está síncrono com a terminação da operação.

### Java

- `Future<R>` é uma interface onde podemos:
  - verificar o seu estado.
  - esperar até que a operação esteja concluída.
  - cancelar 

- `ComputableFuture<R>` é uma classe não abstrata, logo permite a sua instanciação e permite completar explicitamente um future.

```java

val cf = CompletableFuture<string()

cf.complete("Hello")

```

- **.thenApply** - recebe uma função que recebe o resultado do future e retorna um novo future com o resultado da função.
- **.thenCompose** - retorna um futura que é o resultado de aplicar a função ao resultado do future.

- o then do js é equivalente ao thenApply e ao thenCompose do java porque ele sabe se o resultado é uma promisse ou não e faz-lhe flattening.

