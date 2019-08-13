package io.akryl.store

import io.akryl.*
import io.akryl.react.Context
import io.akryl.react.ReactNode
import io.akryl.react.createContext
import io.akryl.rx.ReactiveContainer
import io.akryl.rx.ReactiveHandle
import io.akryl.rx.observable

class StoreContext<T : Store>(
  val context: Context<T?>
) {
  companion object {
    fun <T : Store> create(): StoreContext<T> {
      val context = createContext<T?>(null)
      return StoreContext(context)
    }
  }

  fun provide(store: T, child: ReactNode) = context.provide(observable(store), child)
}

abstract class Store : ReactiveContainer {
  companion object {
    fun <T : Store> of(context: StoreContext<T>): T {
      val store = useContext(context.context)
      return store ?: throw RuntimeException("Store not found")
    }
  }

  final override fun registerReactiveHandle(handle: ReactiveHandle) {}
}
