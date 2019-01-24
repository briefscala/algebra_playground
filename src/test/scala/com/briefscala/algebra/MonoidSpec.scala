package com.briefscala.algebra

import org.scalacheck.{Arbitrary, Properties}
import org.scalacheck.Prop._

abstract class MonoidSpec[A, Op[X] <: Operation[X]](
  m: Monoid[A, Op], name: String)(implicit
  arb: Arbitrary[A]) extends Properties(name) {

  property("leftIdentity") = forAll { a: A =>
    m.combine(m.zero, a) =? a label s"a ia $a"
  }

  property("rightIdentity") = forAll { a: A =>
    m.combine(a, m.zero) =? a label s"a is $a"
  }

  property("associativity") = forAll { (a0: A, a1: A, a2: A) =>
    m.combine(a0, m.combine(a1, a2)) =? m.combine(m.combine(a0, a1), a2) label
      s"a0 is $a0, a1 is $a1 and a2 is $a2"
  }
}

object MonoidSpec {
  val addIntMonoid = Monoid[Int, Add]
  val multIntMonoid = Monoid[Int, Mult]
}

object AddMCForIntSet extends MonoidSpec[Int, Add](
  MonoidSpec.addIntMonoid, "Monoid[Int, Add]")

object MultMCForIntSet extends MonoidSpec[Int, Mult](
  MonoidSpec.multIntMonoid, "Monoid[Int, Mult]")
