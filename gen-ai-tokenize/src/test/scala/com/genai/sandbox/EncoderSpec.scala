package com.genai.sandbox

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class EncoderSpec extends AnyFlatSpec with should.Matchers with BaseSpec {

  val (_, merges) = Tokenizer.mergeTokens(4, unsignedValues, maxId)

  "Encoder#encode" should "encode the input text" in {
    val encoder = new Encoder(merges)
    val encoded = encoder.encode("hello world", merges)
    println(encoded)
    val decodedText = Decoder(updatedVocab).decode(encoded)
    println(decodedText)
    //assert(encoded == unsignedValues)
  }
}
