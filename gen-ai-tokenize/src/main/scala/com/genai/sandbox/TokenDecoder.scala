package com.genai.sandbox

import scala.collection.immutable.ListMap

/**
 * Decoder is a utility class that provides a function to decode a sequence of integers into a string.
 */
final class TokenDecoder(vocabConfig: VocabConfig) {

  def decode(tokens: Seq[Int], vocab: ListMap[String, Int]): String = {
    // invert vocab: Int -> String
    val idToToken: ListMap[Int, String] = vocab.map(_.swap)

    // recursive helper to expand merged tokens like "a_b"
    def expand(tokenStr: String): String = {
      if (!tokenStr.contains(vocabConfig.mergeSeperator)) tokenStr
      else tokenStr.split(vocabConfig.mergeSeperator).map(expand).mkString
    }

    // decode each token ID → token string → expanded form
    tokens.map { id =>
      val token = idToToken.getOrElse(id, vocabConfig.unkToken)
      new String(token.getBytes(vocabConfig.encoding), vocabConfig.encoding) // ensures same byte->char mapping
    }.mkString
  }
}
object TokenDecoder {
  def apply(vocabConfig: VocabConfig): TokenDecoder = new TokenDecoder(vocabConfig)
}