fun <T : Any> Sequence<T>.repeat(times: Int) = sequence{
    val iter = this@repeat.iterator()
    while(iter.hasNext()) {
        val curr = iter.next()
        for(i in 0 until times){
            yield(curr)
        }
    }
}

fun <T : Any> Sequence<T>.repeat(times: Int) = sequence{
        for(i in this){
            for(k in 0 until times){
                yield(i)
            }
        }        
}
