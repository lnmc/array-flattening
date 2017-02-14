package hr.lukanimac

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by luka on 12.02.17.
  */
class FlatteningTests extends FlatSpec with Matchers {

  "An empty array" should "be flattened to an empty array" in {
    assert (ArrayUtils.flatten(Array()) === Array())
  }

  "A one-level nested array with no elements" should "be flattened to an empty array" in {
    assert (ArrayUtils.flatten(Array(Array(), Array())) === Array())
  }

  "Flattening of a one-level nested interleaved array" should "produce correct result" in {
    val nestedArray = Array(Array(), 1, 2, Array(3, 4), Array(5, 6), 7)
    assert (ArrayUtils.flatten(nestedArray) === Array(1, 2, 3, 4, 5, 6, 7))
  }

  "Flattening of a multi-level nested interleaved array" should "produce correct result" in {
    val nestedArray = Array(Array(Array(0)), Array(1, Array()), 2, Array(Array(3, 4)), Array(5, 6), 7)
    assert (ArrayUtils.flatten(nestedArray) === Array(0, 1, 2, 3, 4, 5, 6, 7))
  }

  "Multi-level empty array" should "be flattened to an empty array" in {
    val array = Array[Any](Array(Array()), Array(Array()), Array(Array(Array())), Array())
    assert (ArrayUtils.flatten(array) === Array())
  }

  "Flattening of an array with non-integer element" should "produce an IllegalArgumentExcetion" in {
    val invalidArray = Array[Any](Array(Array("string that does not belong here")), Array(Array()), Array(Array(Array())), Array())
    assertThrows[IllegalArgumentException] {
      ArrayUtils.flatten(invalidArray)
    }
  }
}
