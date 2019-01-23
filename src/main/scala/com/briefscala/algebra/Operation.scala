package com.ereactive.algebra

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
