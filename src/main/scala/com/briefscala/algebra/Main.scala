package com.ereactive.algebra

object Main {

  def main(args: Array[String]): Unit = {

    val addMonoid = Monoid[Int, Add]
    val multMonoid = Monoid[Int, Mult]

    val addSemi = Semigroup[Int, Add]
    val multSemi = Semigroup[Int, Mult]


    implicit val multMonoidCombined = MonoidCombined[Int, Mult]
    implicit val addMonoidCombined = MonoidCombined[Int, Add]

    println(addMonoid.zero)
    println(multMonoid.zero)

    println(addSemi.combine(3, 3))
    println(multSemi.combine(3, 3))

  }
}

