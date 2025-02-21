package com.genai.tokenize

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
  private val maxId = tokens.map(_.toInt & 0xFF).max + 1
  private val merged = mergeTokens(10, tokens, maxId)
  println(tokens)
  println("****************")
  println(merged)

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
  private def getStats(tokens: Seq[Byte], descending: Boolean = true): Map[(Byte, Byte), Int] = {
    ListMap(
      tokens.sliding(2).collect { case Seq(a, b) => (a, b) }
        .toSeq
        .groupBy(identity)
        .view.mapValues(_.size)
        .toSeq
        .sortWith {
          case ((_, count1), (_, count2)) =>
            if (descending) count1 > count2 else count1 < count2
        }: _*  // This is to convert the sorted sequence into the ListMap
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
  private def merge(ids: Seq[Byte], pair: (Byte, Byte), idx: Byte, acc: Seq[Byte] = Seq()): Seq[Byte] = {
    ids match {
      case first +: second +: rest if first == pair._1 && second == pair._2 =>
        merge(rest, pair, idx, acc :+ idx) // Replace the pair and skip the next element
      case first +: rest =>
        merge(rest, pair, idx, acc :+ first) // Keep the element and continue
      case Nil =>
        acc // Return accumulated result when the list is empty
    }
  }

  private def mergeTokens(numMerges: Int, ids: Seq[Byte], maxId: Int): (Seq[Byte], Map[(Byte, Byte), Int]) = {
    @annotation.tailrec
    def loop(i: Int, currentIds: Seq[Byte], merges: Map[(Byte, Byte), Int]): (Seq[Byte], Map[(Byte, Byte), Int]) = {
      val stats = getStats(currentIds).collect { case (pair, count) if count > 1 => pair -> count }

      // Stop if there are no more frequent pairs to merge
      if (stats.isEmpty || i >= numMerges) (currentIds, merges)
      else {
        val (pair, _) = stats.head
        val idx = (maxId + i).toByte
        val newIds = merge(currentIds, pair, idx)
        val newMerges = merges + (pair -> idx.toInt)

        loop(i + 1, newIds, newMerges) // Continue merging
      }
    }

    loop(0, ids, Map.empty)
  }

}
