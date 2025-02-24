package com.genai.sandbox

import com.genai.sandbox
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.nio.charset.StandardCharsets


class EncodeDecodeSpec extends AnyFlatSpec with should.Matchers with BaseSpec {

  val text = "Many common characters, including numerals, punctuation, and other symbols, " +
    "are unified within the standard and are not treated as specific to any given writing system. " +
    "Unicode encodes thousands of emoji, with the continued development thereof conducted by the " +
    "Consortium as a part of the standard.[4] Moreover, the widespread adoption of Unicode was in " +
    "large part responsible for the initial popularization of emoji outside of Japan. Unicode is " +
    "ultimately capable of encoding more than 1.1 million characters."

  override val unsignedValues: Seq[Int] =
    text.getBytes(StandardCharsets.ISO_8859_1)
      .toSeq.
      map(byte => java.lang.Byte.toUnsignedInt(byte))

  val (_, pairs) = Tokenizer.mergeTokens(20, unsignedValues, maxId)
  val vocab: Map[Int, Array[Int]] = sandbox.updatedVocab(pairs)

  "Encode & Decode" should "encode and decode the input text" in {
    val encoded = Encoder.encode(text, pairs)
    println(encoded)
    val decodedText = Decoder(vocab).decode(encoded)
    println(decodedText)
    assert(encoded == unsignedValues)
  }
}
