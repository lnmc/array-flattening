package hr.lukanimac

import scala.annotation.tailrec
import scala.language.existentials

/**
  * Created by luka on 12.02.17.
  */

object ArrayUtils {

  /**
    * Flattens a nested, possibly interleaved array of integers
    * @param array the array to be flattened
    * @return a flat array of integers
    */
  def flatten(array: Array[Any]): Array[Int] = {

    @tailrec
    def flattenRec(remaining: List[(Int, Array[_ >: Int])], accumulator: Vector[Int]): Array[Int] =
      remaining match {
        case Nil =>
          accumulator.toArray

        case (pos, currentArray) :: rest =>
          if (pos < currentArray.length) {
            currentArray match {
              case intArray: Array[Int] =>
                flattenRec(rest, accumulator ++ intArray)

              case anyArray: Array[Any] =>
                anyArray(pos) match {
                  case x: Int =>
                    flattenRec((pos + 1, currentArray) :: rest, accumulator :+ x)

                  case Array() =>
                    flattenRec((pos + 1, currentArray) :: rest, accumulator)

                  case anotherArray : Array[Int]  =>
                    flattenRec((0, anotherArray) :: (pos + 1, currentArray) :: rest, accumulator)

                  case anotherArray : Array[Any] =>
                    flattenRec((0, anotherArray) :: (pos + 1, currentArray) :: rest, accumulator)

                  case somethingElse =>
                    throw new IllegalArgumentException("Invalid array element: " + somethingElse)
                }
            }
          } else {
            flattenRec(rest, accumulator)
          }
      }

      flattenRec(List((0, array)), Vector())
  }

  /**
    * Convenience method for visualising a nested array as a nice String.
    * The method itself is not nice, though.
    *
    * @param array the array
    * @return the string visualisation of the array
    */
  def toString(array: Array[Any]): String = {

    val stringBuilder = new StringBuilder

    def arrayToStringRec(array: Array[Any], needComma: Boolean): Unit = {
      array match {
        case Array() =>

        case Array(x: Int) =>
          if (needComma) stringBuilder ++= ", "
          stringBuilder.append(x)

        case Array((x: Int), xs @ _*) =>
          if (needComma) stringBuilder ++= ", "
          stringBuilder.append(x)
          arrayToStringRec(xs.toArray, needComma = true)

        case Array((x: Array[Int]), xs @ _*) =>
          if (needComma)  stringBuilder ++= ", "
          stringBuilder ++= "("
          arrayToStringRec(x.map (x => x : Any), needComma = false)
          stringBuilder ++= ")"
          arrayToStringRec(xs.toArray, needComma = true)

        case Array((x: Array[Any]), xs @ _*) =>
          if (needComma)  stringBuilder ++= ", "
          stringBuilder ++= "("
          arrayToStringRec(x, needComma = false)
          stringBuilder ++= ")"
          arrayToStringRec(xs.toArray, needComma = true)
      }
    }

    stringBuilder ++= "("
    arrayToStringRec(array, needComma = false)
    stringBuilder ++= ")"
    stringBuilder.toString
  }
}
