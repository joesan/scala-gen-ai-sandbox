package com.genai.sandbox

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.collection.immutable.ListMap
import org.scalatest.Assertions._

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class TokenizerSpec extends AnyFlatSpec with should.Matchers with BaseSpec {

  "Tokenizer#getStats" should "fetch the List of recurrent pairs" in {
    assert(ListMap((97,32) -> 2, (32,116) -> 2, (32,97) -> 1, (116,32) -> 1) == Tokenizer.getStats(bytes))
  }

  "Tokenizer#mergeTokens" should "merge the tokens" in {
    assert(Tokenizer.mergeTokens(4, bytes, maxId)._1 == List(118, 32, 118))
    assert(Tokenizer.mergeTokens(2, bytes, maxId)._1 == List(118, 32, 118))
  }
}
