package com.genai.sandbox

import com.genai.sandbox.TokenEncoder.getStats
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.collection.immutable.ListMap
import org.scalatest.Assertions.*

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class TokenizerSpec extends AnyFlatSpec with should.Matchers with BaseSpec {
/*
  "Tokenizer.mergeTokens(...)" should "deal with invalid inputs" in {
    assertThrows[IllegalArgumentException] {
      BPEApp.mergeTokens(ids = Seq.empty, maxId = maxId)
    }
    assertThrows[IllegalArgumentException] {
      BPEApp.mergeTokens(numMerges = -2, ids = Seq.empty, maxId = maxId)
    }
  }

  "Tokenizer.getStats(...)" should "fetch the List of recurrent pairs" in {
    assert(getStats(unsignedValues) == ListMap((97,32) -> 2, (32,116) -> 2, (32,97) -> 1, (116,32) -> 1))
  }

  "Tokenizer.mergeTokens(...)" should "merge the tokens" in {
    assert(BPEApp.mergeTokens(ids = unsignedValues, maxId = maxId)._1 == List(257, 32, 257))
  } */
}
