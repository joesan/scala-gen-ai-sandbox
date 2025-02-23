package com.genai.sandbox

import scala.collection.immutable.ListMap
import scala.annotation.tailrec


/**
 * Tokenizer is a utility object that provides a function to compute the
 * frequency of consecutive byte pairs in a given sequence.
 */
object Tokenizer extends App {

  // Hyperparameters
  private val vocabSize = 276
  private val numMerges = vocabSize - 256

  private val text = "I a test a test"
  private val tokens: Seq[Byte] = text.getBytes("ISO-8859-1").toSeq
  //private val maxId = tokens.map(_.toInt & 0xFF).max + 1
  private val maxId = 256
  val unsignedValue = tokens.map(byte => java.lang.Byte.toUnsignedInt(byte))
  private val merged = mergeTokens(10, unsignedValue, maxId)
  /*println(tokens)
  println("************")
  println(s"maxId: $maxId")
  println("************")
  println(s"getStats: ${getStats(tokens)}")
  println("************")
  println(merged) */

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
  def getStats(tokens: Seq[Int], descending: Boolean = true): Map[(Int, Int), Int] = {
    ListMap(
      tokens.sliding(2).collect { case Seq(a, b) => (a, b) }
        .toSeq
        .groupBy(identity)
        .view.mapValues(_.size)
        .toSeq
        .sortWith {
          case ((_, count1), (_, count2)) =>
            if (descending) count1 > count2 else count1 < count2
        }: _*
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
  def merge(ids: Seq[Int], pair: (Int, Int), idx: Int, acc: Seq[Int] = Seq()): Seq[Int] = {
    ids match {
      case first +: second +: rest if first == pair._1 && second == pair._2 =>
        merge(rest, pair, idx, acc :+ idx) // Replace pair with new token
      case first +: rest =>
        merge(rest, pair, idx, acc :+ first) // Keep token and continue
      case Nil =>
        acc // Return accumulated result
    }
  }

  def mergeTokens(numMerges: Int, ids: Seq[Int], maxId: Int): (Seq[Int], Map[(Int, Int), Int]) = {
    @annotation.tailrec
    def loop(i: Int, currentIds: Seq[Int], merges: Map[(Int, Int), Int]): (Seq[Int], Map[(Int, Int), Int]) = {
      val stats = getStats(currentIds).collect { case (pair, count) if count > 1 => pair -> count }

      if (stats.isEmpty || i >= numMerges) (currentIds, merges)
      else {
        val (pair, _) = stats.head
        val idx = maxId + i // Keep as Int to prevent overflow

        println(s"Merging $pair -> New Token: $idx")

        val newIds = merge(currentIds, pair, idx)
        val newMerges = merges + (pair -> idx)

        loop(i + 1, newIds, newMerges)
      }
    }

    loop(0, ids, Map.empty)
  }

}
