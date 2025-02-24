package com.genai.sandbox

import com.genai.sandbox.Tokenizer.{getStats, merge}

import java.nio.charset.StandardCharsets
import scala.annotation.tailrec

/**
 * Encoder is a utility class that provides a function to encode a string into a sequence of token IDs.
 *
 * @param merges A map where keys are token pairs `(Int, Int)` that should be merged,
 *               and values are new token indices assigned to the merged pairs.
 */
object Encoder {

  /**
   * Encodes a given string into a sequence of token IDs.
   *
   * @param text   The input string to encode.
   * @param merges A mapping of token pairs to their merged token ID.
   * @return A sequence of token IDs representing the encoded text.
   */
  def encode(text: String, merges: Map[(Int, Int), Int]): Seq[Int] = {
    @tailrec
    def loop(tokens: Seq[Int]): Seq[Int] = tokens match {
      case _ if tokens.length < 2 => tokens // Base case: No more pairs to merge
      case _ =>
        val stats = getStats(tokens) // Compute frequency of adjacent token pairs
        val pair = stats.keys.minBy(p => merges.getOrElse(p, Int.MaxValue))

        merges.get(pair) match {
          case Some(idx) => loop(merge(tokens, pair, idx)) // Merge and continue
          case None => tokens // No more mergeable pairs, return final tokens
        }
    }

    val initialTokens = text.getBytes(StandardCharsets.ISO_8859_1).toSeq.map(byte => java.lang.Byte.toUnsignedInt(byte))
    loop(initialTokens)
  }
}
