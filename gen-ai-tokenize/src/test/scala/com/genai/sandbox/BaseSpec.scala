package com.genai.sandbox

trait BaseSpec {
  val bytes = "a t a t".getBytes("ISO-8859-1").toSeq
  val maxId = bytes.map(_.toInt & 0xFF).max + 1
}
