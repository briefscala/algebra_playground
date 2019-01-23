package com.briefscala.algebra

trait Monoid[T, Op[X] <: Operation[X]] extends Pointed[T, Op] with Semigroup[T, Op]

object Monoid {
  def apply[T, InnerOp[X] <: Operation[X]](implicit innerOp: InnerOp[T])
  : Monoid[T, InnerOp] = new Monoid[T, InnerOp] {
    val op = innerOp
  }
}