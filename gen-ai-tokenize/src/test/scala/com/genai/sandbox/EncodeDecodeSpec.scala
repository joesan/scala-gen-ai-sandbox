package com.genai.sandbox

import com.genai.sandbox
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.nio.charset.StandardCharsets


class EncodeDecodeSpec extends AnyFlatSpec with should.Matchers with BaseSpec {

  val text: String = "Many common characters, including numerals, punctuation, and other symbols, " +
    "are unified within the standard and are not treated as specific to any given writing system. " +
    "Unicode encodes thousands of emoji, with the continued development thereof conducted by the " +
    "Consortium as a part of the standard.[4] Moreover, the widespread adoption of Unicode was in " +
    "large part responsible for the initial popularization of emoji outside of Japan. Unicode is " +
    "ultimately capable of encoding more than 1.1 million characters."

  override val unsignedValues: Seq[Int] =
    text.getBytes(StandardCharsets.ISO_8859_1)
      .toSeq.
      map(byte => java.lang.Byte.toUnsignedInt(byte))

  val (_, mergedPairs) = Tokenizer.mergeTokens(numMerges = 20, ids = unsignedValues, maxId = maxId)
  val vocab: Map[Int, Array[Int]] = sandbox.updatedVocab(mergedPairs)
  println("**********")
  println(vocab.size)

  "Encode & Decode" should "encode and decode the input text" in {
    val encoded = Encoder.encode(text, mergedPairs)
    println(mergedPairs)
    val decodedText: String = Decoder(vocab).decode(encoded)
    println(decodedText)
    assert(decodedText == text)
  }
}
