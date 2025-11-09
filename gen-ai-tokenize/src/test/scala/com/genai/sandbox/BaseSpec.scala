package com.genai.sandbox

import scala.util.Random
import java.nio.charset.StandardCharsets
import scala.collection.immutable.ListMap


trait BaseSpec {

  // Load the default config for BPE
  val bpeConfig: BpeConfig = BPEConfigLoader.load("application.test.conf")
  
  // Instantiate the necessary classes
  val tokenizer: Tokenizer = Tokenizer(bpeConfig.vocabConfig)
  val encoder: TokenEncoder = TokenEncoder(bpeConfig.vocabConfig)
  val decoder: TokenDecoder = TokenDecoder(bpeConfig.vocabConfig)
  
  // Initialize the vocabulary
  val inputVocab: ListMap[String, Int] = tokenizer.buildInputVocab(bpeConfig.inputChars)
  val nextId: Int = inputVocab.size

  /**
   * Generates random words composed of specified characters.
   * @param chars
   * @param count
   * @param seed
   * @return
   *
   * Usage: For a fixed seed for reproducible results call:
   *        randomWords(('a' to 'z'), 1000, Some(42L))
   */
  def randomWords(chars: Seq[Char], count: Int, seed: Option[Long] = None): String = {
    val rand = seed.map(new scala.util.Random(_)).getOrElse(new scala.util.Random())

    def randomWord(): String = {
      val length = if (rand.nextBoolean()) 3 else 4
      (1 to length).map(_ => chars(rand.nextInt(chars.length))).mkString
    }

    (1 to count).map(_ => randomWord()).mkString("\n")
  }
}
