package Utils

class EventTwoParam<T,P> {
    private val observers = mutableSetOf<(T,P) -> Unit>()

    operator fun plusAssign(observer: (T,P) -> Unit) {
        observers.add(observer)
    }

    operator fun minusAssign(observer: (T,P) -> Unit) {
        observers.remove(observer)
    }

    operator fun invoke(value1: T, value2 : P) {
        for (observer in observers)
            observer(value1, value2)
    }
}
