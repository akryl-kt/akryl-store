package io.akryl.store

import io.akryl.*
import io.akryl.rx.EmptyComputedPropertyContainer
import io.akryl.rx.observable
import kotlin.reflect.KClass

abstract class Store : EmptyComputedPropertyContainer {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T : Store> of(context: BuildContext, clazz: KClass<T>): T {
            val state = context
                .ancestorStateOf { (it is StoreProviderState<*>) && clazz.isInstance(it.store) }
                as? StoreProviderState<*>
            return (state?.store as? T) ?: throw RuntimeException("Store with class '$clazz' not found")
        }

        inline fun <reified T : Store> of(context: BuildContext) = of(context, T::class)
    }

    open fun created() {}
}

data class StoreProvider<T : Store>(
    val store: () -> T,
    val child: Widget
) : StatefulWidget() {
    override fun createState(context: BuildContext): State<*> = StoreProviderState<T>(context)
}

class StoreProviderState<T : Store>(context: BuildContext) : State<StoreProvider<T>>(context) {
    val store = observable(widget.store())

    override fun created() {
        super.created()
        store.created()
    }

    override fun build(context: BuildContext) = widget.child
}

data class MultiStoreProvider(
    val modules: List<Store>,
    val child: Widget
) : StatelessWidget() {
    override fun build(context: BuildContext): Widget {
        var result = child
        for (module in modules) {
            result = StoreProvider(
                store = { module },
                child = result
            )
        }
        return result
    }
}
