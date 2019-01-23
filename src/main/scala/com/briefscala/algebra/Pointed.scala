package com.briefscala.algebra

trait Pointed[T, Op[X] <: Operation[X]] {
  def op: Op[T]
  def zero: T = op.zero
}

object Pointed {
  def apply[T, InnerOp[X] <: Operation[X]](implicit innerOp: InnerOp[T]): Pointed[T, InnerOp] =
    new Pointed[T, InnerOp] {
      val op = innerOp
      type Op = InnerOp[T]
    }
}
