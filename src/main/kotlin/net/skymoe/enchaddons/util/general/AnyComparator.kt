package net.skymoe.enchaddons.util.general

class AnyComparator(
    private val hash: (Any?) -> Long,
) : Comparator<Any> {
    override fun compare(
        o1: Any?,
        o2: Any?
    ) = hash(o1).compareTo(hash(o2))
}