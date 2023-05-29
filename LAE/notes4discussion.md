# Notas para a dicussão

## AutoRouter Library

- Biblioteca que permite criação automática de handlers para um `JsonServer` baseado num router com anotações específicas (@AutoRoute)
- Os métodos anotados com `@AutoRoute` e que retornam `Optional`, estão elegíveis para serem handlers.

ArHttpRoute - 
ArHttpHandler - 
ArVerb - 


### Anotações

- **@AutoRoute** - anotação usada por funções de controladores que querem ter um handler http. Para expressar que o método é suposto ser um handler, esta anotação inclui os parâmetros:
  - path - para expressar o path da rota 
  - method - para expressar o método http da rota (Default: GET)
  - isSequence - para expressar caso a rota retorne uma sequência (usado para a P3) (Default: false)

- Os parâmetros das funções de controladores são anotados com:
  - **@ArRoute** - para expressar que o parâmetro é suposto vir na rota/path do request
  - **@ArQuery** - para expressar que o parâmetro é suposto vir no query string do request
  - **@ArBody** - para expressar que o parâmetro é suposto vir no body do request


## JsonServer Library

Biblioteca que usa Javalin para lidar com pedidos http.



# P1 - Reflection API and Types at Runtime

## Reflection API

- Permite um programa de java em **EXECUÇÃO** inspecionar e manipular propriedades internas de objetos, classes e interfaces, em **tempo de execução**.

## Função AutoRouterReflect

- A função de assinatura `Stream<ArHttpRoute> autoRouterReflect(Object controller)`, cria uma stream de rotas para cada função elegível de um dado controlador.
- Através de reflexão obtemos a classe do controlador e por sua vez os métodos declarados (privados e publicos) e vemos se são eligíveis para serem handlers (ter anotação @AutoRoute e retornar Optional) e de seguida criamos um `ArHttpRoute` para cada um deles.
Para criar um `ArHttpRoute` é necessário obter o path e o método http da rota, o handler e caso o método retorne uma sequência.

## Construção do handler do tipo `ArHttpHandlerReflection` 

- Este handler tem um record interno com as específicações de cada parâmetro chamado ParamSpecification que contém o nome do parâmetro, o tipo do parâmetro e uma função mapper que dado um `Map<String, String>` retorna o Objeto com o valor do parâmetro.
- A função de `handle` do handler através dos 3 mapas, cria um array com os parametros da função e invoca a função com os parametros e retorna o resultado.

## Construção dos parâmetros

- Para construir os paramSpecs da função, passa-se os `Parameters` à função buildParamSpecs e a mesma cria um `ParamSpecification` para cada um deles.
- Para criar a função mapper usamos a função `getParamMapper`, que dado o nome e o tipo do parâmetro, caso seja um parâmetro primitivo retorna a função já existente que converte uma string para um dos primitivos, caso contrário significa que o parâmetro é um objeto e devemos construir o seu mapper através de refleção.
- Para isso usamos a função `getObjectMapper` que obtem o construtor do objeto, e os seus parâmetros e cria um array de mappers para cada um deles (considera-se que são todos primitivos) de seguida aplica-se o mapper a cada um dos parâmetros e **tenta-se** cria uma nova instância do objeto com os parâmetros mapeados.

# P2 - Dynamic code generation and Performance evaluation with JMH

# P3 - Lazy senquences

necessário dar um ligeiro sleep para garantir que não há problemas de concorrência na leitura do ficheiro.

através do flush garantimos que o conteúdo é enviado diretamente para o cliente e não fica em buffer

