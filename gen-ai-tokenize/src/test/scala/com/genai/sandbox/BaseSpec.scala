package com.genai.sandbox

import com.genai.sandbox

import java.nio.charset.StandardCharsets

trait BaseSpec {
  val inputText: String = "a t a t"
  val bytes: Seq[Byte] = inputText.getBytes(StandardCharsets.ISO_8859_1).toSeq
  val unsignedValues: Seq[Int] = bytes.map(byte => java.lang.Byte.toUnsignedInt(byte))

  //val maxId = bytes.map(_.toInt & 0xFF).max + 1
  val maxId = 256
}
