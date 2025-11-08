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
  private val tokens: Seq[Byte] = text.getBytes("UTF-8").toSeq
  //private val maxId = tokens.map(_.toInt & 0xFF).max + 1
  private val maxId = 256
  val unsignedValue = tokens.map(byte => java.lang.Byte.toUnsignedInt(byte))

  /**
   * Computes statistics of adjacent token pairs in a sequence.
   *
   * @param tokens     The sequence of token IDs.
   * @param descending If true, sorts by frequency in descending order.
   * @return A map of token pairs and their frequencies, sorted by occurrence.
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
   * Merges occurrences of a specific token pair in the sequence.
   *
   * @param ids  The original sequence of token IDs.
   * @param pair The token pair to be merged.
   * @param idx  The new token ID replacing the pair.
   * @param acc  Accumulator for recursive merging (default: empty sequence).
   * @return The updated sequence with the merged token.
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

  /**
   * Performs Byte Pair Encoding (BPE) on a sequence of tokens.
   *
   * @param numMerges The number of merges to perform.
   * @param ids       The initial sequence of token IDs.
   * @param maxId     The starting ID for newly created tokens.
   * @return A tuple containing the new token sequence and the merge mappings.
   */
  def mergeTokens(numMerges: Int = numMerges, ids: Seq[Int], maxId: Int): (Seq[Int], ListMap[(Int, Int), Int]) = {
    @annotation.tailrec
    def loop(i: Int, currentIds: Seq[Int], merges: ListMap[(Int, Int), Int]): (Seq[Int], ListMap[(Int, Int), Int]) = {
      // Get the count of pairs in descending order, max. pair count will appear first
      val pairStats = getStats(currentIds).collect { case (pair, count) if count > 1 => pair -> count }

      if (pairStats.isEmpty || i >= numMerges) (currentIds, merges)
      else {
        val (pair, _) = pairStats.head
        val idx = maxId + i // Keep as Int to prevent overflow
        println(s"Merging $pair -> New Token: $idx")
        val newIds = merge(currentIds, pair, idx)
        val newMerges = merges + (pair -> idx)
        loop(i + 1, newIds, newMerges)
      }
    }
    require(ids.nonEmpty, "Token sequence cannot be empty")
    require(numMerges >= 0, "Number of merges must be non-negative")
    loop(0, ids, ListMap.empty)
  }
}
