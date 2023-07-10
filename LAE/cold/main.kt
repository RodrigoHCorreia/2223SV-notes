class Foo{
 
fun cold(a: List<Double?>, b: Any) : Double {
    val p = 1234
    var q = a.first()
    if(q == null) { q = p.toDouble() } 
    return q + b as Double
}   
}