package com.genai.sandbox

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.collection.immutable.ListMap
import org.scalatest.Assertions._

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class TokenizerSpec extends AnyFlatSpec with should.Matchers with BaseSpec {

  "Tokenizer#mergeTokens" should "deal with invalid inputs" in {
    assertThrows[IllegalArgumentException] {
      Tokenizer.mergeTokens(ids = Seq.empty, maxId = maxId)
    }
    assertThrows[IllegalArgumentException] {
      Tokenizer.mergeTokens(numMerges = -2, ids = Seq.empty, maxId = maxId)
    }
  }

  "Tokenizer#getStats" should "fetch the List of recurrent pairs" in {
    assert(ListMap((97,32) -> 2, (32,116) -> 2, (32,97) -> 1, (116,32) -> 1) == Tokenizer.getStats(unsignedValues))
  }

  "Tokenizer#mergeTokens" should "merge the tokens" in {
    assert(Tokenizer.mergeTokens(ids = unsignedValues, maxId = maxId)._1 == List(257, 32, 257))
    assert(Tokenizer.mergeTokens(ids = unsignedValues, maxId = maxId)._1 == List(257, 32, 257))
  }
}
