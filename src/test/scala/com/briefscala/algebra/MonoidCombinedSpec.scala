package com.briefscala.algebra

import org.scalacheck.{Arbitrary, Properties}
import org.scalacheck.Prop.forAll

abstract class MonoidCombinedSpec[A, Op[X] <: Operation[X]](
  m: MonoidCombined[A, Op], name: String)(implicit
  arb: Arbitrary[A]) extends Properties(name) {

  property("leftIdentity") = forAll { a: A =>
    m.combine(m.zero, a) == a
  }

  property("rightIdentity") = forAll { a: A =>
    m.combine(a, m.zero) == a
  }

  property("associativity") = forAll { (a0: A, a1: A, a2: A) =>
    m.combine(a0, m.combine(a1, a2)) == m.combine(m.combine(a0, a1), a2)
  }
}

object MonoidCombinedSpec {
  val addIntMonoidCombined = MonoidCombined[Int, Add]
  val multIntMonoidCombined = MonoidCombined[Int, Mult]
}

object AddMCForIntSet extends MonoidCombinedSpec[Int, Add](
  MonoidCombinedSpec.addIntMonoidCombined, "MonoidCombined[Int, Add]")

object MultMCForIntSet extends MonoidCombinedSpec[Int, Mult](
  MonoidCombinedSpec.multIntMonoidCombined, "MonoidCombined[Int, Mult]")
