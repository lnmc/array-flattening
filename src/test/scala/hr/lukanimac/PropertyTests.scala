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

  property("flattened nested array should be equal to the generated flat array") {
    forAll(nestedAndFlattenedArrayGen(maxDepth)) {
      case (nestedArray, flatArray) =>
        ArrayUtils.flatten(nestedArray) === flatArray
    }
  }

  property("flattening of a flat array should result with the equal array") {
    forAll (Gen.containerOf[Array, Any](arbitrary[Int])) {
      (array: Array[Any]) =>
        ArrayUtils.flatten(array) should equal (array)
    }
  }

  property("flattening of the nested empty array should result with an empty array") {
    forAll (nestedEmptyArrayGen(maxDepth)) {
      (array: Array[Any]) =>
        ArrayUtils.flatten(array) should equal (Array())
    }
  }

  property("flattening if an array with single nesting level should give the same result as the ArrayOps flatten") {
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
