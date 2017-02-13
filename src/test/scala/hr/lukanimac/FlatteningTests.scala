package hr.lukanimac

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by luka on 12.02.17.
  */
class FlatteningTests extends FlatSpec with Matchers {

  "Flatten of an empty array" should "result with an empty array" in {
    assert (ArrayUtils.flatten(Array()) === Array())
  }

  "Flatten of a one-level nested array with no elements" should "result with an empty array" in {
    assert (ArrayUtils.flatten(Array(Array(), Array())) === Array())
  }

  "Flatten of a one-level nested interleaved array" should "produce correct result" in {
    assert (ArrayUtils.flatten(Array(Array(), 1, 2, Array(3, 4), Array(5, 6), 7)) === Array(1, 2, 3, 4, 5, 6, 7))
  }

  "Flatten of a multi-level nested interleaved array" should "produce correct result" in {
    val array = Array(Array(Array(0)), Array(1, Array()), 2, Array(Array(3, 4)), Array(5, 6), 7)
    assert (ArrayUtils.flatten(array) === Array(0, 1, 2, 3, 4, 5, 6, 7))
  }

  "Flatten of a multi-level empty array" should "result with an empty array" in {
    val array = Array[Any](Array(Array()), Array(Array()), Array(Array(Array())), Array())
    assert (ArrayUtils.flatten(array) === Array())
  }
}
