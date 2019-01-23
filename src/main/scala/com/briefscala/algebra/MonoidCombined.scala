package com.ereactive.algebra

trait MonoidCombined[T, Op[X] <: Operation[X]] extends Monoid[T, Op] with Semigroup[T, Op]

object MonoidCombined {
  def apply[T, InnerOp[X] <: Operation[X]](implicit innerOp: InnerOp[T])
  : MonoidCombined[T, InnerOp] = new MonoidCombined[T, InnerOp] {
    val op = innerOp
  }
}