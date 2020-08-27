package escape.com.todomvp.util

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger


class SimpleCountingIdlingResource(val resourceName: String) : IdlingResource {

    private val counter = AtomicInteger(0)

    @Volatile private var resourceCallBack: IdlingResource.ResourceCallback? = null

    override fun getName() = resourceName

    override fun isIdleNow() = counter.get() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallBack = callback
    }

    fun increment() {
        counter.getAndIncrement()
    }

    fun decrement() {
        val counterVal = counter.getAndDecrement()
        if(counterVal == 0) {
            resourceCallBack?.onTransitionToIdle()
        }

        if(counterVal < 0) {
            throw IllegalArgumentException("Counter has been corrupted!")
        }
    }
}