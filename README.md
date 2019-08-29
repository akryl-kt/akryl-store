# Akryl Store

Reactive store library for Akryl.

## Usage

Define store class:

```kotlin
class CounterStore : Store() {
  // state
  var count = 0

  // action
  fun increment() {
    count += 1
  }
}
```

Define context to use store inside components:

```kotlin
val CounterContext = StoreContext.create<CounterStore>()
```

Use store inside components:

```kotlin
class CounterView : Component() {
  override fun render(): ReactNode {
    val store = Store.of(CounterContext)
    return Div(
      Text("Count = ${store.count}"),
      Button(text = "Increment", onClick = { store.increment() })
    )
  }
}
```
