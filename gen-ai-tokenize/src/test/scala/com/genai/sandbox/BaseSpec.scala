package com.genai.sandbox

import scala.util.Random
import java.nio.charset.StandardCharsets


trait BaseSpec {
  val inputChars: Seq[Char] = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9') :+ ' '
  val inputVocab: Map[String, Int] =
    inputChars.zipWithIndex.map { case (ch, idx) => ch.toString -> idx }.toMap
  println(s"Input Vocabulary Size: ${inputVocab.size}")

  // Input contains 7 bytes - 4 characters and 3 spaces
  val inputText: String = "a t a t"

  // bytes will now be - ArraySeq(97, 32, 116, 32, 97, 32, 116)
  val bytes: Seq[Byte] = inputText.getBytes(StandardCharsets.UTF_8).toSeq
  val unsignedValues: Seq[Int] = bytes.map(byte => java.lang.Byte.toUnsignedInt(byte))

  //val maxId = bytes.map(_.toInt & 0xFF).max + 1
  val maxId = 256

  /**
   * Generates random words composed of specified characters.
   * @param chars
   * @param count
   * @param seed
   * @return
   *
   * Usage: For a fixed seed for reproducible results call:
   *        randomWords(('a' to 'z'), 1000, Some(42L))
   */
  def randomWords(chars: Seq[Char], count: Int, seed: Option[Long] = None): String = {
    val rand = seed.map(new scala.util.Random(_)).getOrElse(new scala.util.Random())

    def randomWord(): String = {
      val length = if (rand.nextBoolean()) 3 else 4
      (1 to length).map(_ => chars(rand.nextInt(chars.length))).mkString
    }

    (1 to count).map(_ => randomWord()).mkString("\n")
  }
}
