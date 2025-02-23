package com.genai.sandbox

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.collection.immutable.ListMap

class DecoderSpec extends AnyFlatSpec with should.Matchers with BaseSpec {
  val (seqs, mergedPairs) = Tokenizer.mergeTokens(4, unsignedValues, maxId)
  // Step 1: Initialize vocabulary (mapping 0-255 to their integer representations)
  private val vocab: ListMap[Int, Array[Int]] =
    ListMap((0 until 256).map(i => i -> Array(i)): _*)

  // Step 2: Update the vocab with merged token pairs
  val updatedVocab: Map[Int, Array[Int]] = mergedPairs.foldLeft(vocab) {
    case (currentVocab, ((p0, p1), idx)) =>
      val token1 = currentVocab(p0) // No need for & 0xFF as it's already Int
      val token2 = currentVocab(p1)
      currentVocab + (idx -> (token1 ++ token2)) // Merge and update
  }

  // Print out the updated vocab
  println("***********************")
  updatedVocab.foreach { case (idx, value) =>
    println(s"$idx -> ${value.map(_.toChar).mkString}")
  }
  println("***********************")

  println("---------")
  println(seqs)
  println(mergedPairs)
  println("---------")

  "something" should "do something" in {
    val decoder = Decoder(updatedVocab)
  }
}
