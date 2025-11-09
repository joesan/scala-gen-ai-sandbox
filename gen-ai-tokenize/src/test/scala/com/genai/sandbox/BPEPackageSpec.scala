package com.genai.sandbox

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should


class BPEPackageSpec extends AnyFlatSpec with should.Matchers with BaseSpec {

  "getStats" should "count adjacent pairs correctly" in {
    val tokens = Seq(1, 2, 3, 1, 2)
    val stats = getStats(tokens)

    // Check that the pairs are correct
    assert(stats.contains((1, 2)))
    assert(stats.contains((2, 3)))
    assert(stats((1, 2)) == 2) // appears twice
    assert(stats((2, 3)) == 1) // appears once
  }

  "getStats" should "return pairs sorted in descending order by default" in {
    val tokens = Seq(1, 2, 1, 2, 3)
    val stats = getStats(tokens)

    // The first pair should be the most frequent
    val mostFrequentPair = stats.head._1
    assert(mostFrequentPair == (1, 2))
  }

  "getStats" should "sort pairs in ascending order if descending=false" in {
    val tokens = Seq(1, 2, 1, 2, 3)
    val stats = getStats(tokens, descending = false)

    // The first pair should be the least frequent
    val leastFrequentPair = stats.head._1
    assert(leastFrequentPair == (2, 1))
  }

  "getStats" should "handle empty input" in {
    val tokens = Seq.empty[Int]
    val stats = getStats(tokens)
    assert(stats.isEmpty) // no pairs in empty sequence
  }

  "getStats" should "handle a single token input" in {
    val tokens = Seq(42)
    val stats = getStats(tokens)
    assert(stats.isEmpty) // no adjacent pairs possible
  }
}
