package com.genai.sandbox

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.collection.immutable.ListMap
import org.scalatest.Assertions.*

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class TokenizerSpec extends AnyFlatSpec with should.Matchers with BaseSpec {

  "Tokenizer.buildInputVocab" should "build a correct ListMap with <unk> at the end" in {
    val inputChars = Some(Seq('a', 'b', 'c'))
    val vocab = tokenizer.buildInputVocab(inputChars)

    // Check that all input characters are included
    assert(vocab.contains("a"))
    assert(vocab.contains("b"))
    assert(vocab.contains("c"))

    // Check that <unk> token is added at the end
    assert(vocab.contains("<unk>"))
    assert(vocab("<unk>") == vocab.size - 1) // last index
  }

  "Tokenizer.tokenize" should "convert a string to token IDs using the vocab" in {
    val inputChars = Some(Seq('a', 'b', 'c'))
    val vocab = tokenizer.buildInputVocab(inputChars)

    val text = "abc"
    val tokenIds = tokenizer.tokenize(text, vocab)

    // Each character should map to its correct index
    assert(tokenIds == Seq(vocab("a"), vocab("b"), vocab("c")))
  }

  "Tokenizer.tokenize" should "map unknown characters to <unk> token ID" in {
    val inputChars = Some(Seq('a', 'b', 'c'))
    val vocab = tokenizer.buildInputVocab(inputChars)

    val text = "abx" // 'x' is unknown
    val tokenIds = tokenizer.tokenize(text, vocab)

    // 'x' should map to <unk>
    assert(tokenIds == Seq(vocab("a"), vocab("b"), vocab("<unk>")))
  }

  "Tokenizer.tokenize" should "handle an empty string" in {
    val vocab = tokenizer.buildInputVocab(Some(Seq('a', 'b')))
    val tokenIds = tokenizer.tokenize("", vocab)

    // Should return an empty sequence
    assert(tokenIds.isEmpty)
  }
}
