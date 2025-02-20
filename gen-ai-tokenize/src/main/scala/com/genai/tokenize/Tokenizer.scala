package com.genai.tokenize

import scala.collection.immutable.ListMap
import scala.annotation.tailrec


/**
 * Tokenizer is a utility object that provides a function to compute the
 * frequency of consecutive byte pairs in a given sequence.
 */
object Tokenizer extends App {

  private val text = "This is a test for the sake of testing the test a test for the testing purposes another a test Zz ZZ Zz ÿ"
  private val tokens: Seq[Byte] = text.getBytes("ISO-8859-1").toSeq
  val maxId = tokens.map(_.toInt & 0xFF).max
  println(maxId)
  print(getStats(tokens))

  /**
   * Computes the frequency of consecutive byte pairs in a given sequence, sorted in descending order by frequency.
   *
   * This function takes a sequence of bytes and counts how often each consecutive byte pair appears.
   * It then returns the results sorted in descending order based on their frequency.
   *
   * @param tokens A sequence of bytes (e.g., UTF-8 encoded text).
   * @return A sorted map where each key is a tuple of two consecutive bytes, and the value represents
   *         the count of occurrences of that pair, sorted in descending order.
   * @example
   * {{{
   *   val tokens: Seq[Byte] = "hello".getBytes("UTF-8")
   *   val stats = getStats(tokens)
   *   println(stats)  // Example output: Map((108,108) -> 1, (104,101) -> 1, (101,108) -> 1, (108,111) -> 1)
   * }}}
   */
  private def getStats(tokens: Seq[Byte]): Map[(Byte, Byte), Int] = {
    ListMap(
      tokens.sliding(2).collect { case Seq(a, b) => (a, b) }
        .toSeq
        .groupBy(identity)
        .view.mapValues(_.size)
        .toSeq
        .sortWith(_._2 > _._2): _* // Sort descending
    )
  }

  /**
   * Merges a sequence of integers by replacing consecutive occurrences of a specified pair with a new token.
   * This function is tail-recursive for optimized performance.
   *
   * @param ids  The sequence of integers to process.
   * @param pair The pair of consecutive integers to replace.
   * @param idx  The new integer that replaces occurrences of `pair`.
   * @param acc  An accumulator storing the modified sequence (default: empty sequence).
   * @return A new sequence with all occurrences of `pair` replaced by `idx`.
   */
  @tailrec
  private def merge(ids: Seq[Int], pair: (Int, Int), idx: Int, acc: Seq[Int] = Seq()): Seq[Int] = {
    ids match {
      case first +: second +: rest if first == pair._1 && second == pair._2 =>
        merge(rest, pair, idx, acc :+ idx) // Replace the pair and skip the next element
      case first +: rest =>
        merge(rest, pair, idx, acc :+ first) // Keep the element and continue
      case Nil =>
        acc // Return accumulated result when the list is empty
    }
  }


}
