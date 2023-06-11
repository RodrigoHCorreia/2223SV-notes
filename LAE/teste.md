Ex 2

método estático genérico VarUtils.getPublicVarGettersOf
retorna mapa<nome da propriedade publicamente mutável do tipo T, instância de Method correspondente aos respetivos métodos getter>
EXCETO as anotadas com @get:DontRead 

a. escrever a anotação @DontRead, aplicável somente a getters, apresente em java o método getPublicVarGettersOf

dontRead.kt:
```kotlin
@Target(ElementType.METHOD) // This applies to all methods and not only getters.
public @interface DontRead
```

foo.kt:
```kotlin

public class <T> VarUtils {

    public static <T> Map<String, Function<T, Object>> getPublicVarGettersOf(Class<T> clazz) {
        
    }
}
```
Anotações:

1 programador define

1 programador usa


## Todo:
- lesson 14 - fazer ex