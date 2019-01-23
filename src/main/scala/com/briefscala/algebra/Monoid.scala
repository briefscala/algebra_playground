package com.ereactive.algebra

trait Monoid[T, Op[X] <: Operation[X]] {
  def op: Op[T]
  def zero: T = op.zero
}

object Monoid {
  def apply[T, InnerOp[X] <: Operation[X]](implicit innerOp: InnerOp[T]): Monoid[T, InnerOp] =
    new Monoid[T, InnerOp] {
      val op = innerOp
      type Op = InnerOp[T]
    }
}
