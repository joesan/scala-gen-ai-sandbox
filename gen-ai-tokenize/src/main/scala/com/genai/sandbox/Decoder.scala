package com.genai.sandbox

/**
 * Decoder is a utility class that provides a function to decode a sequence of integers into a string.
 *
 * @param vocabulary A map where each key is an integer, and the value is an array of integers.
 */
class Decoder(val vocabulary: Map[Int, Array[Int]]) {

  /**
   * Decodes a sequence of integers into a string using the provided vocabulary.
   *
   * @param tokens A sequence of integers representing tokens.
   * @return A string decoded from the input sequence.
   */
  def decode(tokens: Seq[Int]): String = {
    tokens.map(vocabulary).flatMap(_.map(_.toByte)).map(_.toChar).mkString
  }
}
