# Aula 15 - Meta-programming, Dynamic Code Generation

## Geração Dinâmica de Código

Capacidade para ler, gerar, transformar ou modificar programas **dinamicamente** (em runtime)

Bibliotecas: 
- .net - System.Reflection.Emit
- Java - ASM, Javassist, **Cojen Maker**, etc.

## Cojen Maker
> github library - https://github.com/cojen/Maker
> 
- Biblioteca a usar no desenvolvimento do trabalho

### Caracteristicas

- É uma API **fluente**, pois podemos encadear chamadas de métodos
- O nosso código tem de garantir a propriedade **Safety**, algo relativamente fácil de fazer com o Cojen, porque dá a falha logo em compilação (não em runtime) e é mais fácil de debugar
- Não há manipulação do **stack**.

### How to use

- Incluir a biblioteca no gradle (implementation("org.cojen:cojen-maker:2.4.7"))

### Organização

- Tipo **Variable** - representa qualquer coisa (variáveis, tipos, expressões, etc.)

- ClassMaker - permite criar classes
- MethodMaker - permite criar métodos ou construtores através de addMethod() e addConstructor()
- FieldMaker - permite criar campos através de addField()
- através de .finish() podemos obter a classe criada
- Class **tem** Member
- Method, Constructor e Field **são** Member

### Exemplo 

```java
class MyDynamicType {
    private final int nr;

    public MyDynamicType(int nr) {
        this.nr = nr;
    }

    public int mul(int other) {
        return other * nr;
    }
}

```

```java

ClassMaker classMaker = ClassMaker.begin().public_()

/**
 * Field nr
 */
FieldMaker nrMaker = classMaker.addField(int.class, "nr").private_().final_();

/**
 * Constructor
 */
MethodMaker ctorMaker = classMaker
                                .addConstructor(int.class)
                                .public_();

ctorMaker.invokeSuperConstructor();

ctorMaker
    .field(nrMaker.name())
    .set(ctorMaker.param(0));

/**
 * Method mul
 */
MethodMaker mulMaker = classMaker
                            .addMethod(int.class, "mul", int.class)
                            .public_();
Variable res = mulMaker
    .param(0)
    .mul(nrMaker.field(nrMaker.name()))

mulMaker.return_(res);


Class<?> myDynamicType = classMaker.finish();

Object target = myDynamicType
                    .getDeclaredConstructor(int.class)
                    .newInstance(9);

int result = myDynamicType
    .getMethod("mul", int.class)
    .invoke(target, 3);

System.out.println(result); // 27
```

#### TPC 

- chamar o getMethod via interface para melhorar a performance
- arranjar uma interface que tem um método abstrato mul e quando chamada a instancia podemos fazer cast para a interface e chamar o método
- poderemos chamar diretamente target.mul(3) e não precisamos de fazer o invoke
- dizendo que o classMaker .implements(MyInterface.class)