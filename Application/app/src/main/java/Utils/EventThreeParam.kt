package Utils

class EventThreeParam<T,P,C> {
    private val observers = mutableSetOf<(T,P,C) -> Unit>()

    operator fun plusAssign(observer: (T,P,C) -> Unit) {
        observers.add(observer)
    }

    operator fun minusAssign(observer: (T,P,C) -> Unit) {
        observers.remove(observer)
    }

    operator fun invoke(value1: T, value2 : P, value3: C) {
        for (observer in observers)
            observer(value1, value2, value3)
    }
}
