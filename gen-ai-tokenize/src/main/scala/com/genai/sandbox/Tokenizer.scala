package com.genai.sandbox

import scala.collection.immutable.ListMap

/**
 * A simple tokenizer that maps characters to unique IDs based on a provided vocabulary.
 */
final class Tokenizer(vocabConfig: VocabConfig) {

  /** Builds the input vocabulary from either config characters or full byte set */
  def buildInputVocab(inputCharsOpt: Option[Seq[Char]]): ListMap[String, Int] = {
    val baseItems: Seq[(String, Int)] = inputCharsOpt match {
      case Some(inputChars) =>
        inputChars.map(_.toString).zipWithIndex
      case None =>
        (0 to 255).map(i => i.toChar.toString -> i)
    }
    val itemsWithUnk = baseItems :+ (vocabConfig.unkToken -> baseItems.size)
    ListMap.from(itemsWithUnk)
  }

  /**
   * Tokenize a text string into token IDs using the given vocabulary.
   */
  def tokenize(text: String, inputVocab: ListMap[String, Int]): Seq[Int] = {
    text.getBytes(vocabConfig.encoding).map { b =>
      val token = new String(Array(b), vocabConfig.encoding)  // convert byte to string token
      inputVocab.getOrElse(token, inputVocab(vocabConfig.unkToken))
    }.toSeq
  }
}
object Tokenizer {
  def apply(vocabConfig: VocabConfig): Tokenizer = new Tokenizer(vocabConfig)
}