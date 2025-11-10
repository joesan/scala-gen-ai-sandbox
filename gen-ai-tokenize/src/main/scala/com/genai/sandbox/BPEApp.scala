package com.genai.sandbox

import scala.collection.immutable.ListMap
import scala.io.Source
import scala.util.Using

/**
 * BPEApp demonstrates the process of tokenizing, encoding (merging), and decoding text using Byte Pair Encoding (BPE).
 */
object BPEApp extends App {

  // 1. Load configuration
  val config = BPEConfigLoader.load()

  println("*" * 60)
  println("🚀 Starting BPE Tokenization with configuration:\n")
  // Pretty print key-values from the config
  prettyPrintConfig(obj = config)
  println("\n" + "*" * 60)

  // 2. Initialize components
  val tokenizer = new Tokenizer(config.vocabConfig)
  val encoder = new TokenEncoder(config.vocabConfig)
  val decoder = new TokenDecoder(config.vocabConfig)

  // 3. Read input text from file
  val inputText: String = Using.resource(Source.fromResource(config.files.inputFile)) { source =>
    source.getLines().mkString("\n")
  }
  println(s"📘 Loaded input from: ${config.files.inputFile}")
  println(s"🔹 Sample: ${inputText.take(100)}...\n")

  // 4. Build initial vocab from config
  val inputVocab: ListMap[String, Int] = tokenizer.buildInputVocab(config.inputChars)

  // 5. Tokenize the input
  val tokenIds: Seq[Int] = tokenizer.tokenize(inputText, inputVocab)
  println(s"🔹 Tokenized IDs: $tokenIds")

  // 6. Encode (this will merge pairs and update vocab)
  val encodedOutput = encoder.encode(
    tokens = tokenIds,
    vocab = inputVocab,
    merges = Map.empty,
    nextId = inputVocab.size
  )

  println(s"🔹 Encoded tokens: ${encodedOutput.encodedTokens}")
  println(s"🔹 Updated vocab: ${encodedOutput.updatedVocab}")
  println(s"🔹 Merges: ${encodedOutput.merged}")

  // 7. Decode back to original text
  val decodedText = decoder.decode(
    encodedOutput.encodedTokens,
    encodedOutput.updatedVocab
  )

  println(s"✅ Decoded text: $decodedText")
}
