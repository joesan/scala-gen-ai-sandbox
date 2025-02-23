package com.genai.sandbox

trait BaseSpec {
  val bytes = "a t a t".getBytes("ISO-8859-1").toSeq
  val unsignedValues = bytes.map(byte => java.lang.Byte.toUnsignedInt(byte))
  //val maxId = bytes.map(_.toInt & 0xFF).max + 1
  val maxId = 256
}
