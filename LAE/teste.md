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



## Ex 3 (Exame normal)

```java

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidText{
    String[] valids
}

public String checkAndSet(Object obj, String propName, String toCheck) {
        Clazz<*> clazz = obj.getClass()
        Field field = clazz.getDeclaredField(propName);
        Annotation annotation = field.getAnnotation(ValidText.class);
        if(annotation==null){
            throw new AnnotationNotFoundException("Annotation ValidText not in property");
        }
        List<String> list = Arrays.asList(annotation.valids);

        if(list.contains(toCheck)){
            String setterName = "set" + propName.substring(0,1).toUpperCase() + propName.substring(1)
            Method setter    = clazz.getDeclaredMethod(setterName);
            if(setter==null){
                throw new IllegalArgumentException();
            }
            setter.invoke(obj, toCheck);
            return toCheck;
        } else {
            throw new IllegalArgumentException();
        }
}

```

## ex 7 EN

```kotlin
inline fun <reified T> listOfDefaults(n : Int = 0) : List<T> {
    val list = mutableListOf<T>()
    repeat(n){
        list.add(T::class.java.constructors[0].newInstance() as T)
    }
    return list 
}


// Exemplo de utilização
fun main() {
    val list1 = listOfDefaults<Student>(3)
    val list2 : List<Person> = listOfDefaults(2)
// ...
}

```

## Todo:
- lesson 14 - fazer ex