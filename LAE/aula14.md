# Aula 14 - Bytecode para dinamizar código

- javap (permite ver) -> metadata, bytecode
- para ver metadata e bytecode usamos -c
- para ver a constante pool usamos -v

- **Construtor de Instância**
  - iniciar campos de instâncias
  - Nome: <init>
- **Construtor static:**
  - iniciar campos estáticos
- **Constant Pool**
  - Tabela de referências para constantes

- **Cada instrução ocupa 3 bytes**, 1 para o bytecode, 2 para o index da constante pool
- Existem instruções autosuficientes, que não precisam de index, como é o caso do **aconst_null**

## Invokes 

- **Desempenho:**
  - invokeInterface < invokeVirtual < invokeSpecial < invokeStatic

- **Reflexão:**
  - invokeStatic < invokeSpecial < invokeVirtual < invokeInterface

- **invokestatic**
  - invoca um método estático
  - não passa um Receiver (this)
  
- **invokespecial**
  - invoca um método de instância
  - passa um Receiver (this)
  - chamadas não polimórficas equivalente a despacho estático
  - resolvido em tempo de compilação pelo jitter
  

- **invokevirtual**
  - invoca um método de instância
  - passa um Receiver (this)
  - chamadas polimórficas (diferentes formas) equivalente a despacho dinâmico
  - resolvido em tempo de execução

- **invokeinterface**
  - invoca um método de interface
  - passa um Receiver (this)
  - chamadas polimórficas (diferentes formas) equivalente a despacho dinâmico

## Variáveis Locais

_ load _ (push Static)
_ store _ (pop Static)
prefixo (tipo)
sufixo (index)

- **Tipos de Variáveis Locais:**
  - _i_ (int)
  - _l_ (long)
  - _f_ (float)
  - _d_ (double)
  - ...

## Instânciação de Objetos

Quando é instânciado um objeto, é criado um espaço na memória para o objeto e para os seus campos de instância.

- **new**
  - cria um objeto
  - aloca memória para o objeto e para os seus campos de instância
  - inicializa os campos de instância com valores default
  - invoca o construtor de instância
  - devolve uma referência para o objeto criado

- **dup**
  - duplica o valor no topo da stack
  - usado para passar o receiver para o construtor

- é necessário duplicar o valor porque o retorno do construtor é void logo perdia-se a referência para o objeto criado, uma delas é consumida pelo construtor e a outra é devolvida para a stack

- **invokespecial**
  - invoca o construtor de instância
  - passa o receiver (this)


`invokevirtual`: for invoking instance methods (with support for polymorphism)
`invokestatic`: for invoking static methods
`invokespecial`: for invoking constructors, private methods, or superclass methods
`invokeinterface`: for invoking interface methods
`invokedynamic`: for dynamic method invocation in non-Java languages running on the JVM
## Posters

- implicito é tudo aquilo que não vemos mas é gerado
- Sempre que virmos um new, iremos ver um dup a seguir.