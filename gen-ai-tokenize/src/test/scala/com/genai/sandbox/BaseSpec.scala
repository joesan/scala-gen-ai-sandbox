package com.genai.sandbox

import com.genai.sandbox

import java.nio.charset.StandardCharsets

trait BaseSpec {
  // Input contains 7 bytes - 4 characters and 3 spaces
  val inputText: String = "a t a t"
  
  // bytes will now be - ArraySeq(97, 32, 116, 32, 97, 32, 116)
  val bytes: Seq[Byte] = inputText.getBytes(StandardCharsets.ISO_8859_1).toSeq
  val unsignedValues: Seq[Int] = bytes.map(byte => java.lang.Byte.toUnsignedInt(byte))

  //val maxId = bytes.map(_.toInt & 0xFF).max + 1
  val maxId = 256
}
