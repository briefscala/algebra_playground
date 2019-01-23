package com.ereactive.algebra

trait Semigroup[A, Op[X] <: Operation[X]] {
  def op: Op[A]
  def combine(a1: A, a2: A): A = op.combine(a1, a2)
}

object Semigroup {
  def apply[T, InnerOp[X] <: Operation[X]](implicit innerOp: InnerOp[T]): Semigroup[T, InnerOp] =
    new Semigroup[T, InnerOp] {
      val op = innerOp
      type Op = InnerOp[T]
    }
}