package hr.lukanimac

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

/**
  * Created by luka on 12.02.17.
  */

object ArrayUtils {

  def flatten(array: Array[Any]): Array[Int] = {

    @tailrec
    def flattenRec(remaining: List[Any], accumulator: ArrayBuffer[Int]): Array[Int] =
      remaining match {
        case Nil =>
          accumulator.toArray

        case Array() :: xs =>
          flattenRec(xs, accumulator)

        case (x: Int) :: xs =>
          flattenRec(xs, accumulator += x)

        // cases such as this one are covered by the case following it, but separating it helps avoid an unnecessary ++
        case Array(x: Int) :: xs =>
          flattenRec(xs, accumulator += x)

        case Array(x: Int, xs @ _* ) :: ys =>
          flattenRec(xs ++: ys, accumulator += x)

        case Array(xa: Array[Int]) :: ys =>
          flattenRec(ys, accumulator ++= xa)

        case Array(xa: Array[Int], xs @ _*) :: ys =>
          flattenRec(xs ++: ys, accumulator ++= xa)

        case Array(xa: Array[Any]) :: ys =>
          flattenRec(xa ++: ys, accumulator)

        case Array(xa: Array[Any], xs @ _*) :: ys =>
          flattenRec(xa ++: xs ++: ys, accumulator)

        case z => throw new IllegalArgumentException("Unsupported element: " + z)
      }

      flattenRec(array.toList, new ArrayBuffer[Int]())
  }

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
