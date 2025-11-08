package com.genai.sandbox

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

/**
 * TokenEncoder provides functionality to merge token pairs in a sequence
 * according to Byte Pair Encoding (BPE) rules.
 */
object TokenEncoder {

  /**
   * Computes statistics of adjacent token pairs in a sequence.
   *
   * @param tokens     The sequence of token IDs.
   * @param descending If true, sorts by frequency in descending order.
   * @return A map of token pairs and their frequencies, sorted by occurrence.
   */
  def getStats(tokens: Seq[Int], descending: Boolean = true): ListMap[(Int, Int), Int] =
    ListMap.from {
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
   * Merge pairs in a sequence of tokens according to BPE rules.
   *
   * @param tokens       The current token sequence
   * @param vocab The current vocab mapping token string -> ID
   * @param merges The current merges mapping token pairs -> merged ID
   * @param nextId       Next available token ID
   * @return EncodedOutput(newTokens, updatedMerges, updatedVocab, newNextId)
   */
  def merge(tokens: Seq[Int], vocab: ListMap[String, Int], merges: Map[(Int, Int), Int], nextId: Int): EncodedOutput = {
    /** Tail-recursive merge loop */
    @tailrec
    def loop(tokens: Seq[Int], merges: Map[(Int, Int), Int], vocab: ListMap[String, Int], nextId: Int): EncodedOutput = tokens match {
      case Seq(_) | Seq() => 
        EncodedOutput(tokens, merges, vocab, nextId)
      case _ =>
        val stats = getStats(tokens)
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

          // Merge first occurrences of the pair
          val newTokens = {
            @tailrec
            def mergeLoop(acc: Vector[Int], rem: Seq[Int]): Vector[Int] = rem match {
              case a +: b +: tail if (a, b) == pair =>
                mergeLoop(acc :+ mergedId, tail)
              case head +: tail =>
                mergeLoop(acc :+ head, tail)
              case Nil => acc
            }
            mergeLoop(Vector.empty, tokens)
          }

          if (newTokens == tokens) EncodedOutput(tokens, merges, vocab, nextId)
          else loop(newTokens, updatedMerges, updatedVocab, updatedNextId)
        }
    }

    loop(tokens, merges, vocab, nextId)
  }
}

