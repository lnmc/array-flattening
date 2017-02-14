package hr.lukanimac

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Matchers, PropSpec}


/**
  * Created by luka on 12.02.17.
  */
class PropertyTests extends PropSpec with GeneratorDrivenPropertyChecks with Matchers {

  val maxGenArraySize = 5
  val maxDepth = 10

  property("flatten produces consistent results") {
    forAll(nestedAndFlattenedArrayGen(maxDepth)) {
      case (nestedArray, flatArray) =>
        ArrayUtils.flatten(nestedArray) === flatArray
    }
  }

  property("flatten on a non-nested array does not alter the array") {
    forAll (Gen.containerOf[Array, Any](arbitrary[Int])) {
      (array: Array[Any]) =>
        ArrayUtils.flatten(array) should equal (array)
    }
  }

  property("flatten on a nested array with no integer elements produces an empty array") {
    forAll (nestedEmptyArrayGen(maxDepth)) {
      (array: Array[Any]) =>
        ArrayUtils.flatten(array) should equal (Array())
    }
  }

  property("flatten on an array with single nesting level corresponds to the result of flatten in ArrayOps") {
    forAll (Gen.containerOf[Array, Array[Int]](Gen.containerOf[Array, Int](arbitrary[Int]))) {
      (array: Array[Array[Int]]) =>
        ArrayUtils.flatten(array.map(x => x : Any)) should equal (array.flatten)
    }
  }

  def nestedArrayGen(maxDepth: Int): Gen[Array[Any]] = {
    def constantElements: Gen[Any] = Gen.frequency(
      (20, arbitrary[Int]),
      (1,  Gen.const(Array())))

    Gen.containerOfN[Array, Any] (maxGenArraySize,
      Gen.frequency(
        (10, constantElements),
        (10, if (maxDepth > 0) nestedArrayGen(maxDepth - 1) else constantElements)
      )
    )
  }

  def nestedAndFlattenedArrayGen(maxDepth: Int): Gen[(Array[Any], Array[Int])] = {
    def constantElements: Gen[(Any, Array[Int])] = Gen.frequency(
      (20, arbitrary[Int].flatMap(i => (i, Array(i)))),
      (1,  Gen.const( (Array(), Array[Int]()) )))

    Gen.containerOfN[Array, (Any, Array[Int])] (maxGenArraySize,
      Gen.frequency(
        (10, constantElements),
        (10, if (maxDepth > 0) nestedAndFlattenedArrayGen(maxDepth - 1) else constantElements)
      )
    ).flatMap(_.unzip match { case (nestedArray, flatArray) => (nestedArray, flatArray.flatten) })
  }

  def nestedEmptyArrayGen(maxDepth: Int): Gen[Array[Any]] =
    Gen.containerOfN[Array, Any] (maxGenArraySize,
      Gen.frequency(
        (10, Gen.const(Array())),
        (10, if (maxDepth > 0) nestedEmptyArrayGen(maxDepth - 1) else Gen.const(Array()))
      )
    )
}
