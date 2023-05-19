# Aula 29 - Sequence Exercises

```kotlin
fun <T> Sequence<T>.collapse(): Sequence<T> = sequence {
    val iter = this@collapse.iterator()
    lateinit var prev: T
    for(item in iter){
        if(this::prev.isInitialized || prev != item){
            yield(item)
        }
        prev = item
    }
}
```

1. Iterador da Source
2. a) Vazio? (iter.hasNext() == false -> return)
   b) != {} (prev = iter.next() + yield(prev))
3. ciclo hasNext() / next() != prev -> prev = yield(next())

Interleave
```kotlin
fun <T> Sequence<T>.interleave(other: Sequence<T>): Sequence<T> = sequence {
    val iterThis = this@interleave.iterator()
    val iterOther = other.iterator()
    while(iterThis.hasNext() || iterOther.hasNext()){
        if(iterThis.hasNext()){
            yield(iterThis.next())
        }
        if(iterOther.hasNext()){
            yield(iterOther.next())
        }
    }
}
```

Concat
```kotlin
fun <T> Sequence<T>.concat(other: Sequence<T>): Sequence<T> = sequence {
    val iterThis = this@interleave.iterator()
    val iterOther = other.iterator()
    while(iterThis.hasNext()){
        yield(iterThis.next())
    }
    while(iterOther.hasNext()){
        yield(iterOther.next())
    }	
}
```

Window
```kotlin
fun <T> Sequence<T>.window(size: Int): Sequence<Sequence<T>> = sequence {
    val iter = this@window.iterator()
    while(iter.hasNext()){
        val currentAmount = 0
        val currentSequence = sequence {
            while(currentAmount < size && iter.hasNext()){
                yield(iter.next())
                currentAmount++
            }
        }
        yield(currentSequence)
    }
}
```

## Yield Magic

