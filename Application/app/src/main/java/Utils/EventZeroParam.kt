package Utils

class EventZeroParam {
    private val observers = mutableSetOf<() -> Unit>()

    operator fun plusAssign(observer: () -> Unit) {
        observers.add(observer)
    }

    operator fun minusAssign(observer: () -> Unit) {
        observers.remove(observer)
    }

    operator fun invoke() {
        for (observer in observers)
            observer()
    }
}