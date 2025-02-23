package com.genai.sandbox

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.collection.immutable.ListMap

class DecoderSpec extends AnyFlatSpec with should.Matchers with BaseSpec {
  val (seqs, mergedPairs) = Tokenizer.mergeTokens(4, unsignedValues, maxId)
  private val vocab: ListMap[Int, Array[Byte]] = (0 until 256).map(i => i -> Array(i.toByte)).to(ListMap)
  // Step 2: Update the vocab with merged byte pairs
  val updatedVocab: Map[Int, Array[Byte]] = mergedPairs.foldLeft(vocab) { case (currentVocab, ((p0, p1), idx)) =>
    currentVocab + (idx -> (currentVocab(p0.toInt  & 0xFF) ++ currentVocab(p1.toInt  & 0xFF)))
  }

  // Print out the updated vocab
  updatedVocab.foreach { case (idx, value) =>
    println(s"$idx -> ${value.map(_.toChar).mkString}")
  }

  println("---------")
  println(seqs)
  println(mergedPairs)
  println("---------")
  
  "something" should "do something" in {
    val decoder = Decoder(updatedVocab)
  }
}
