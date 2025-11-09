package com.genai.sandbox

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

/**
 * TokenEncoder provides functionality to merge token pairs in a sequence
 * according to Byte Pair Encoding (BPE) rules.
 */
object TokenEncoder {

  private type Pair = (Int, Int)
  private type Count = Int
  private type TokenID = Int

  /**
   * Computes statistics of adjacent token pairs in a sequence.
   *
   * @param tokens     The sequence of token IDs.
   * @param descending If true, sorts by frequency in descending order.
   * @return A map of token pairs and their frequencies, sorted by occurrence.
   */
  def getStats(tokens: Seq[Int], descending: Boolean = true): ListMap[Pair, Count] = ListMap.from {
    tokens.sliding(2).collect { case Seq(a, b) => (a, b) }
      .toSeq
      .groupBy(identity)
      .view.mapValues(_.size)
      .toSeq
      .sortWith {
        case ((_, count1), (_, count2)) =>
          if (descending) count1 > count2 else count1 < count2
      }
  }

  /**
   * Merges occurrences of a specific token pair in the sequence.
   *
   * @param ids  The original sequence of token IDs.
   * @param pair The token pair to be merged.
   * @param idx  The new token ID replacing the pair.
   * @param acc  Accumulator for recursive merging (default: empty vector).
   * @return The updated sequence with the merged token.
   */
  @tailrec
  def mergePairs(ids: Seq[Int], pair: (Int, Int), idx: Int, acc: Vector[Int] = Vector.empty): Vector[Int] = ids match {
    case a +: b +: tail if (a, b) == pair =>
      mergePairs(tail, pair, idx, acc :+ idx)
    case head +: tail =>
      mergePairs(tail, pair, idx, acc :+ head)
    case Nil =>
      acc
  }

  /**
   * Merge pairs in a sequence of tokens according to BPE rules.
   *
   * @param tokens The current token sequence
   * @param vocab  The current vocab mapping token string -> ID
   * @param merges The current merges mapping token pairs -> merged ID
   * @param nextId Next available token ID
   * @return EncodedOutput(newTokens, updatedMerges, updatedVocab, newNextId)
   */
  def merge(tokens: Seq[Int], vocab: ListMap[String, TokenID], merges: Map[(Int, Int), Int], nextId: Int): EncodedOutput = {
    /** Tail-recursive merge loop */
    @tailrec
    def loop(tokens: Seq[Int], merges: Map[(Int, Int), Int], vocab: ListMap[String, TokenID], nextId: Int): EncodedOutput = tokens match {
      case Seq(_) | Seq() => 
        EncodedOutput(tokens, merges, vocab, nextId)
      case _ =>
        val stats: ListMap[Pair, Count] = getStats(tokens)
        if (stats.isEmpty) EncodedOutput(tokens, merges, vocab, nextId)
        else {
          val pair = stats.keys.minBy(p => merges.getOrElse(p, Int.MaxValue))
          val (mergedId, updatedVocab, updatedMerges, updatedNextId) =
            merges.get(pair) match {
              case Some(id) =>
                (id, vocab, merges, nextId)
              case None =>
                val name1 = vocab.find(_._2 == pair._1).get._1
                val name2 = vocab.find(_._2 == pair._2).get._1
                val newToken = s"${name1}_${name2}"
                val newVocab = vocab + (newToken -> nextId)
                val newMerges = merges + (pair -> nextId)
                (nextId, newVocab, newMerges, nextId + 1)
            }

          // Merge all occurrences of the pair
          val newTokens = mergePairs(tokens, pair, mergedId)

          if (newTokens == tokens) EncodedOutput(tokens, merges, vocab, nextId)
          else loop(newTokens, updatedMerges, updatedVocab, updatedNextId)
        }
    }

    loop(tokens, merges, vocab, nextId)
  }
}
