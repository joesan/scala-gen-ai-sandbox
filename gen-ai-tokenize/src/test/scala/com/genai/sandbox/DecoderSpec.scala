package com.genai.sandbox

import com.genai.sandbox
import com.genai.sandbox.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.collection.immutable.ListMap


class DecoderSpec extends AnyFlatSpec with should.Matchers with BaseSpec {

  "Decoder#decode" should "decode the input token id to string representation" in {
    val decodedText = Decoder(updatedVocab).decode(unsignedValues)
    assert(inputText == decodedText)
  }
}
