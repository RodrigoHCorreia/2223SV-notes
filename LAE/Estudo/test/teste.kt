package Estudo.test;

public class teste {
    val strs = mutableListOf("ISEL", "LEIC", "LAE")
    
    strs[2] as Int = 5
    (strs as Array<Int>)[2] = 5
    (strs as MutableList<Int>).add(5)
    strs.add(5 as String)

}
