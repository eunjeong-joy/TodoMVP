package escape.com.todomvp.util

object EspressoIdlingResource {
    private val RESOURCE = "GLOBAL"

    val countingIdlingResource = SimpleCountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        countingIdlingResource.decrement()
    }
}