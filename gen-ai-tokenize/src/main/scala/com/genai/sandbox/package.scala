package com.genai

import scala.collection.immutable.ListMap

package object sandbox {

  private type Pair = (Int, Int)
  private type Count = Int
  private type TokenID = Int

  case class EncodedOutput(
    encodedTokens: Seq[Int], 
    merged: Map[(Int, Int), Int], 
    updatedVocab: ListMap[String, Int], 
    nextTokenId: Int
  )
  
  case class VocabConfig(encoding: String, maxMerges: Int, mergeSeperator: String = "_", unkToken: String = "<unk>")
  case class TokenizationConfig(minPairFrequency: Int, includeWhitespace: Boolean)
  case class FilesConfig(vocabFile: String, mergesFile: String, inputFile: String)
  case class BpeConfig(
    vocabConfig: VocabConfig,
    inputChars: Option[Seq[Char]] = None,
    tokenization: TokenizationConfig,
    files: FilesConfig
  )

  // Recursive pretty-print function with alignment
  def prettyPrintConfig(obj: Any, prefix: String = ""): Unit = obj match {
    case prod: Product =>
      prod.productIterator.zip(prod.getClass.getDeclaredFields).foreach { case (value, field) =>
        val name = field.getName
        value match {
          case p: Product =>
            println(s"$prefix$name:")
            prettyPrintConfig(p, prefix + "  ")
          case seq: Seq[_] =>
            println(s"$prefix$name = [${seq.mkString(", ")}]")
          case map: Map[_, _] =>
            println(s"$prefix$name = {${map.map { case (k, v) => s"$k -> $v" }.mkString(", ")}}")
          case v =>
            println(s"$prefix$name = $v")
        }
      }
    case _ =>
      println(s"$prefix$obj")
  }

  /**
   * Builds a byte-level vocabulary mapping each byte value (0-255)
   * to its corresponding single-character string representation.
   *
   * @return ListMap[String, Int] mapping each character (as a String) to its byte value
   */
  def buildByteVocab(vocabConfig: VocabConfig): ListMap[String, Int] = {
    // Create a sequence of byte values from 0 to 255
    val bytes: Seq[Int] = 0 until 256

    // Represent each byte as a string safely using ISO-8859-1 (1:1 mapping)
    val tokens: Seq[String] = bytes.map { b =>
      new String(Array(b.toByte), "ISO-8859-1") // maps byte directly to single char
    }

    // Build the base vocabulary: each character maps to its byte value
    val baseVocab = ListMap(tokens.zip(bytes) *)

    // Add a special <unk> token for unknown bytes
    baseVocab + (vocabConfig.unkToken -> baseVocab.size)
  }

  /**
   * Generates a vocabulary from the provided input vocabulary mapping.
   *
   * @param inputVocab A map where keys are string tokens and values are their corresponding integer IDs.
   * @return A `ListMap` where each key is an integer ID, and the value is an array containing that ID.
   */
  def vocab(inputVocab: Map[String, Int]): ListMap[Int, Array[Int]] = {
    ListMap.from(inputVocab.values.map(id => id -> Array(id)))
  }

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
   * Updates the vocabulary by merging token pairs into new tokens.
   *
   * @param mergedPairs A map where keys are token pairs `(Int, Int)` that should be merged,
   *                    and values are new token indices assigned to the merged pairs.
   * @return A `Map` where merged tokens are replaced with new token representations.
   */
  /*def updatedVocab(mergedPairs: Map[(Int, Int), Int]): Map[Int, Array[Int]] = {
    mergedPairs.foldLeft(vocab()) {
      case (currentVocab, ((p0, p1), idx)) =>
        println()
        val token1 = currentVocab(p0) // Retrieve the first token
        val token2 = currentVocab(p1) // Retrieve the second token
        currentVocab + (idx -> (token1 ++ token2)) // Merge and update
    }
  } */
}
