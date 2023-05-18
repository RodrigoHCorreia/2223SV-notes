# Aula 28 

- **Closeable** -> devemos ser nós a fechar o recurso .use { }
-se n fosse chamado o closeable explicitamente, o GC iria correr o cleaner.

- PhantomReference -> permite-nos que o GC não identifique o objeto como vivo.

**oracle java cleaner**

- **The cleaning action could be a lambda but all too easily will capture the object reference, by referring to fields of the object being cleaned, preventing the object from becoming phantom reachable.**

## Generics - Type Erasure

- genéricos provide:
- **type safety** - permite-nos garantir que o tipo dos elementos de uma coleção é o que esperamos e não podemos fazer utilizações ilegais dos elementos da coleção
- **expressiveness** - permite-nos expressar o tipo dos elementos de uma coleção

### Life Before Generics

```java
static vois lifeBeforeGenerics(){
    List labels = asList("one", "two", "three", 7);
    String first = (String) labels.get(0);
    out.println(first.length());
}
```

**Type Erasure:**
- é o processo de remover os parâmetros de tipo e substituir os parâmetros de tipo por Object
- Isto é feito ao passar o source code para bytecode
- por esta razão ao usarmos o cojenmaker, o código gerado não tem generics e temos de fazer casting explicito ao fazer gets de coleções.