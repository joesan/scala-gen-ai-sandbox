package com.genai.sandbox

import com.genai.sandbox
import com.genai.sandbox.TokenEncoder.getStats
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.collection.immutable.ListMap


class TokenEncoderSpec extends AnyFlatSpec with should.Matchers with BaseSpec {
/*
  // Set up the test data
  val (seqs, mergedPairs) = BPEApp.mergeTokens(4, unsignedValues, maxId)
  val updatedVocab: Map[Int, Array[Int]] = sandbox.updatedVocab(mergedPairs)

  val (_, merges) = BPEApp.mergeTokens(4, unsignedValues, maxId)

  "Encoder#encode" should "encode the input text" in {
    val encoded = Encoder.encode("hello world", merges)
    println(encoded)
    val decodedText = Decoder(updatedVocab).decode(encoded)
    println(decodedText)
    //assert(encoded == unsignedValues)
  } */

  "TokenEncoder.getStats(...)" should "fetch the List of recurrent pairs" in {
    val tokens = Tokenizer.tokenize("a t a t", inputVocab)
    assert(getStats(tokens) == ListMap((97, 32) -> 2, (32, 116) -> 2, (32, 97) -> 1, (116, 32) -> 1))
  }

  "TokenEncoder.merge(...)" should "merge & update the vocab" in {
    // --- Create a small test input (pick chars from your vocab) ---
    val inputText: String = "Many common characters, including numerals, punctuation, and other symbols, " +
      "are unified within the standard and are not treated as specific to any given writing system. " +
      "Unicode encodes thousands of emoji, with the continued development thereof conducted by the " +
      "Consortium as a part of the standard.[4] Moreover, the widespread adoption of Unicode was in " +
      "large part responsible for the initial popularization of emoji outside of Japan. Unicode is " +
      "ultimately capable of encoding more than 1.1 million characters."
    // Convert text to token IDs using Tokenizer
    val tokens = Tokenizer.tokenize(inputText, inputVocab)

    // --- When ---
    val encodedOutput = TokenEncoder.merge(tokens, inputVocab, Map.empty[(Int, Int), Int], nextId)
    // --- Then ---
    assert(encodedOutput.encodedTokens.nonEmpty, "Encoded tokens should not be empty")
    assert(encodedOutput.encodedTokens.length < tokens.length, "Some pairs should be merged")
    assert(encodedOutput.updatedVocab.keys.exists(_.contains("_")), "Vocab should include merged entries")
    assert(encodedOutput.merged.nonEmpty, "Merges should not be empty")
    assert(encodedOutput.nextTokenId > nextId, "Next ID should increment after merge")
    println(s"Updated Vocab: ${encodedOutput.updatedVocab}")
  }
}
