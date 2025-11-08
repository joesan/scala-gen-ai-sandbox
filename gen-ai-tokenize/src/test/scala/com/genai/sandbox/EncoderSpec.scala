package com.genai.sandbox

import com.genai.sandbox
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class EncoderSpec extends AnyFlatSpec with should.Matchers with BaseSpec {

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
  }
}
