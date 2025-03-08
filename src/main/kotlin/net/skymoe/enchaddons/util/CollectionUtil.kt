package net.skymoe.enchaddons.util

infix fun <T> Collection<T>.prepend(element: T): List<T> =
    buildList(this.size + 1) {
        add(element)
        addAll(this@prepend)
    }

infix fun <T> T.prependTo(collection: Collection<T>): List<T> =
    buildList(collection.size + 1) {
        add(this@prependTo)
        addAll(collection)
    }

inline fun <T> MutableList<T>.inPlaceMap(transformer: (T) -> T): MutableList<T> {
    listIterator().apply {
        while (hasNext()) {
            set(transformer(next()))
        }
    }
    return this
}
