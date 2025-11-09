package com.genai.sandbox

import scala.collection.immutable.ListMap

/**
 * A simple tokenizer that maps characters to unique IDs based on a provided vocabulary.
 */
final class Tokenizer(vocabConfig: VocabConfig) {

  /**
   * Build the input vocabulary from a sequence of characters.
   * Adds a special <unk> token for unknown characters.
   */
  def buildInputVocab(inputChars: Seq[Char]): ListMap[String, Int] = {
    val baseVocab = ListMap.from(inputChars.zipWithIndex.map { case (ch, idx) => ch.toString -> idx })
    baseVocab + (vocabConfig.unkToken -> baseVocab.size)  // add <unk> at the end
  }

  /** Builds the input vocabulary from either config characters or full byte set */
  def buildInputVocab(inputCharsOpt: Option[Seq[Char]]): ListMap[String, Int] = {
    val vocab = inputCharsOpt match {

      // Case 1: Build from provided characters in config
      case Some(inputChars) => buildInputVocab(inputChars)

      // Case 2: Fallback to full byte-level vocab (0–255)
      case None => buildByteVocab(vocabConfig)
    }

    // Add <unk> token at the end
    vocab + (vocabConfig.unkToken -> vocab.size)
  }

  /**
   * Tokenize a text string into token IDs using the given vocabulary.
   */
  def tokenize(text: String, inputVocab: ListMap[String, Int]): Seq[Int] = {
    text.map(ch => inputVocab.getOrElse(ch.toString, inputVocab(vocabConfig.unkToken)))
  }
}
object Tokenizer {
  def apply(vocabConfig: VocabConfig): Tokenizer = new Tokenizer(vocabConfig)
}