package com.genai

import scala.collection.immutable.ListMap

package object sandbox {

  private val maxInitialVocabSize = 256

  // Hyperparameters TODO: Get these from a conf file externally
  private val vocabSize = 276
  private val numMerges = vocabSize - 256

  /**
   * Generates an initial vocabulary mapping each integer from `0` to `max - 1`
   * to an array containing that integer.
   *
   * @param max The maximum size of the initial vocabulary (default: `maxInitialVocabSize`).
   * @return A `ListMap` where each key is an integer, and the value is an array containing that integer.
   */
  def vocab(max: Int = maxInitialVocabSize): ListMap[Int, Array[Int]] = {
    ListMap.from((0 until max).map(i => i -> Array(i)))
  }

  /**
   * Updates the vocabulary by merging token pairs into new tokens.
   *
   * @param mergedPairs A map where keys are token pairs `(Int, Int)` that should be merged,
   *                    and values are new token indices assigned to the merged pairs.
   * @return A `Map` where merged tokens are replaced with new token representations.
   */
  def updatedVocab(mergedPairs: Map[(Int, Int), Int]): Map[Int, Array[Int]] = {
    mergedPairs.foldLeft(vocab()) {
      case (currentVocab, ((p0, p1), idx)) =>
        val token1 = currentVocab(p0) // Retrieve the first token
        val token2 = currentVocab(p1) // Retrieve the second token
        currentVocab + (idx -> (token1 ++ token2)) // Merge and update
    }
  }
}
