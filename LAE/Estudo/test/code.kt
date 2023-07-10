annotation class NotInUse

@NotInUse data class XY (val x: Int, val y: Int)

val seq2 = sequence {
    yield(sequence { yield(1); yield(2); yield(3);});
    yield(sequenceOf(4, 5, 6))
}

object SeqPrinter { fun <T> print(s: Sequence<T>) { s.forEach(::println)}}

fun main() { seq2.forEach(SeqPrinter::print) }