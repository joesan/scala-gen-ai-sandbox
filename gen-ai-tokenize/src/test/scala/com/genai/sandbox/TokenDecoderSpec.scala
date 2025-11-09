package com.genai.sandbox

import com.genai.sandbox
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.collection.immutable.ListMap


class TokenDecoderSpec extends AnyFlatSpec with should.Matchers with BaseSpec {

  "TokenDecoder.decode" should "convert token IDs back to original text" in {
    val vocab = ListMap(
      "a" -> 0,
      "b" -> 1,
      "c" -> 2,
      "<unk>" -> 3,
      "ab" -> 4
    )
    val encodedTokens = Seq(4, 2) // corresponds to "ab" + "c"

    val decodedText = decoder.decode(encodedTokens, vocab)

    // Assert that decoding gives the expected original string
    assert(decodedText == "abc")
  }

  "TokenDecoder.decode" should "replace unknown tokens with <unk> or fallback" in {
    val vocab = ListMap(
      "a" -> 0,
      "b" -> 1,
      "<unk>" -> 2
    )
    val encodedTokens = Seq(0, 1, 99) // 99 does not exist in vocab

    val decodedText = decoder.decode(encodedTokens, vocab)

    // Assert that unknown token IDs are decoded as <unk>
    assert(decodedText == "ab<unk>")
  }

  "TokenDecoder.decode" should "handle empty input" in {
    val vocab = ListMap("a" -> 0)
    val encodedTokens = Seq.empty[Int]

    val decodedText = decoder.decode(encodedTokens, vocab)

    // Assert that decoding an empty token sequence returns empty string
    assert(decodedText.isEmpty)
  }

  "TokenDecoder.decode" should "decode merged tokens correctly" in {
    val vocab = ListMap(
      "h" -> 0,
      "e" -> 1,
      "l" -> 2,
      "o" -> 3,
      "he" -> 4,
      "ll" -> 5,
      "lo" -> 6
    )
    val encodedTokens = Seq(4, 5, 3) // corresponds to "he" + "ll" + "o"

    val decodedText = decoder.decode(encodedTokens, vocab)

    // Assert that merged tokens are correctly converted back to original text
    assert(decodedText == "hello")
  }

  "TokenDecoder.decode" should "perform round-trip encoding and decoding" in {
    val inputText = "abc"
    val vocab = ListMap(
      "a" -> 0,
      "b" -> 1,
      "c" -> 2,
      "ab" -> 3,
      "<unk>" -> 4
    )

    val tokens = tokenizer.tokenize(inputText, vocab)
    val encodedTokens = encoder.encode(tokens, vocab, Map.empty, nextId)
    val decodedText = decoder.decode(encodedTokens.encodedTokens, encodedTokens.updatedVocab)

    // Assert that decoding the encoded text returns the original input
    assert(decodedText == inputText)
  }
}
