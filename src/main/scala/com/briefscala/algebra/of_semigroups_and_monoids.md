## Semigroups and Monoids in Scala

### Semigroups

In this is a brief introduction to binary operations in Scala with `Semigroup` and `Monoid` we'll take the pragmatic approach and will skip the mathematical details. A semigroup is a term that comes from Mathematics used for talking about algebraic structures that can be combined i.e. Sets with a combine binary operation. The result of the combination falls withing the same initial Set. In Scala we say that the result of of the same type. Let's look a straight forward way to represent this abstraction in Scala with a few examples.

```scala
trait Semigroup[T] {
  def combine(t1: T, t2: T): T
}

val addIntSemigroup = new Semigroup[Int] {
  def combine(t1: Int, t2: Int): Int = t1 + t2
}

val addStringSemigroup = new Semigroup[String] {
  def combine(t1: String, t2: String): String = t1 + t2
}

val multIntSemigroup = new Semigroup[Int] {
  def combine(t1: Int, t2: Int): Int = t1 * t2
}
```

So, it really is as simple as we said. Given two values of the same type, or the same Set, if we have a `Semigroup` for that type we know how to combine them. This sounds like a really useful thing to want to do and it certainly is but it isn't without some challenges. When designing our own `Semigroup` we most ensure that the associative rule is obeyed.

```scala
val isValidMonoid = combine(x1, combine(x2, x3)) == combine(combine(x1, x2), x3)
```

As an exercise do you think that the following `combine` method is a valid one to use in a `addStringSemigroup` implementation?

```scala
def combine(t1: String, t2: String): String = t1 + " " + t2
```

Inspecting the `Semigroup` examples given we can already see that we have two different ways for combining `Int` values but before we tackle this challenge lets first look at what is a `Monoid`.

### Pointed

If you thought that semigroups were trivial and were needless of such obscure name, monoids are even simpler (and arguably with an even more obscure name). A monoid is the addition of a Pointed Set, that is a Set with only one element, to a semigroup such that the lone element serves as the identity element. This identity element is called `zero` conventionally and it the identity for the semigroup combine operation only. A different operation could need a different element as `zero`. Let's take a look at what would that look like in Scala with a few simple examples.

```scala
trait Pointed[T] {
  def zero: T
}

trait Monoid[T] extends Pointed[T] with Semigroup[T]

val addIntPointed = new Monoid[Int] {
  def zero: Int = 0
}

val addStringPointed = new Monoid[String] {
  def zero: String = ""
}
 
val multIntPointed = new Monoid[Int] {
  def zero: Int = 1
}
```

There is truly nothing surprising or obscure about `Monoid`s. Just like we need operations like `sum`, `multiplication` and `substraction` in the real world for example for banking or shopping we also need a `zero` and in Mathematics, Set Theory or Category Theory, this is given with respect to an operation by the `Monoid` construct which have to obey the identity law too for all the elements in the Set.

```scala
val isValidMonoid = combine(elem, zero) == elem
```

### Operations

Both monoids and semigroups give us a way to talk about properties of a binary operation over the elements in a Set. In this post I want to have a way to talk about these operations in isolation of monoids and semigroups so I am going to abstract them separately.

```scala
sealed abstract class Operation[T] {
  def combine(t1: T, t2: T): T
  def zero: T
}

sealed trait Add[T] extends Operation[T]

sealed trait Mult[T] extends Operation[T]

object Operation {
  implicit val multInt: Mult[Int] = new Mult[Int] {
    def combine(t1: Int, t2: Int): Int = t1 * t2
    def zero: Int = 1
  }

  implicit val addInt: Add[Int] = new Add[Int] {
    def combine(t1: Int, t2: Int): Int = t1 + t2
    def zero: Int = 0
  }
}
```

The abstract class `Operation` contains the binary operation `combine` needed by semigroups and `zero` needed by monoids. We tucked away the implementation for the `Add[Int]` and for the `Mult[Int]` operations in the `Operation` companion object so that they can be discovered later when we summon them implicitly. With this in hand I am going to modify our `Monoid` and `Semigroup` traits to take advantage of the `Operation` that we just defined above.

```scala
trait Pointed[A] {
  def zero: A
}

trait Semigroup[A, Op[X] <: Operation[X]] {
  def op: Op[A]
  def append(a1: A, a2: A): A = op.combine(a1, a2)
}

trait Monoid[A, Op[X] <: Operation[X]] extends Pointed[A] with Semigroup[A, Op]
```

Both traits now take a second type parameter, an operation to act on or to act with is you prefer it. `Semigroup` delegates the implementation of `append` to the given operation and `Monoid` that of `zero`. So if this our new traits for `Monoid` and `Semigroup` what would their implementation look like?

```scala
object Monoid {
  def apply[T, InnerOp[X] <: Operation[X]](implicit innerOp: InnerOp[T])
  : Monoid[T, InnerOp] =
    new Monoid[T, InnerOp] {
      val op = innerOp
      type Op = InnerOp[T]
    }
}

object Semigroup {
  def apply[T, InnerOp[X] <: Operation[X]](implicit innerOp: InnerOp[T])
  : Semigroup[T, InnerOp] =
    new Semigroup[T, InnerOp] {
      val op = innerOp
      type Op = InnerOp[T]
    }
}
```

Actually because we abstracted away their operations we can now implement them generically as above and once you have work through the type constraints this is quite simple. So what can we do with our `Monoid` and `Semigroup`?

```scala
val addMonoid = Monoid[Int, Add]
val multMonoid = Monoid[Int, Mult]

val addSemi = Semigroup[Int, Add]
val multSemi = Semigroup[Int, Mult]

println(addMonoid.zero) // 0
println(multMonoid.zero) // 1

println(addSemi.append(3, 3)) // 6
println(multSemi.append(3, 3)) // 9
```

Because we can differentiate operations on `Int`s and we differentiate `Monoid`s and `Semigroup`s by their types and operations they act on, these are also known as `TypeFamily`, we can have an addition monoid and a multiplication monoid for `Int`s and the same for semigroups.

It also makes practical sense to have these two trait behaviors into one trait. We are going to do that here in a trait that I am going to called `MonoidCombined` for added clarity which would give us a way to talk generically about these operations.

```scala
trait MonoidCombined[T, Op[X] <: Operation[X]] 
  extends Monoid[T, Op] with Semigroup[T, Op]

object MonoidCombined {
  def apply[T, InnerOp[X] <: Operation[X]](implicit innerOp: InnerOp[T])
  : MonoidCombined[T, InnerOp] = new MonoidCombined[T, InnerOp] {
    val op = innerOp
  }
}

implicit val multMonoidCombined = MonoidCombined[Int, Mult]

implicit val addMonoidCombined = MonoidCombined[Int, Add]

def withMonoidCombined(int1: Int, int2: Int)(
  implicit addIntMC: MonoidCombined[Int, Add]
): Int = addIntMC.combine(int1, int2)

withMonoidCombined(3, 3) // 6
``` 

In this post we gloss over the rigour of mathematics, I would argue that for our benefits and without lose of meaning, however our new `Semigroup`, `Monoid` and `MonoidCombined` it more closely resembles its definition in Set Theory sporting greater flexibility for precising which operation we mean when calling for a monoid in a Set i.e which monoid we mean in `Monoid[Int]` since many are possible under the right `Operation`. For a more rigours analysis spend a 5 minutes with Bartosz Milewski in this lecture [here](https://youtu.be/aZjhqkD6k6w?t=1968).
