package com.genai.sandbox

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should


class BPEPackageSpec extends AnyFunSpec with should.Matchers with BaseSpec {

  describe("buildByteVocab") {

    it("should create a vocabulary with 256 entries") {
      val vocab = buildByteVocab(bpeConfig.vocabConfig)
      // Assert that vocab has exactly 256 entries
      assert(vocab.size == 257)
    }

    it("should map ASCII/UTF-8 characters correctly") {
      val vocab = buildByteVocab(bpeConfig.vocabConfig)
      // Assert some sample character mappings
      assert(vocab("A") == 'A'.toInt) // 'A' -> 65
      assert(vocab("a") == 'a'.toInt) // 'a' -> 97
      assert(vocab("0") == '0'.toInt) // '0' -> 48
      assert(vocab(" ") == ' '.toInt) // space -> 32
      assert(vocab("\u0000") == 0) // first byte
      assert(vocab("\u00FF") == 255) // last byte
    }

    it("should preserve insertion order") {
      val vocab = buildByteVocab(bpeConfig.vocabConfig)
      val firstKey = vocab.head._1
      val secondLastKey = vocab.dropRight(1).last._1
      val lastKey = vocab.last._1

      // Assert first byte (0x00)
      assert(firstKey == "\u0000")
      // Assert last real byte (0xFF)
      assert(secondLastKey == "\u00FF")
      // Assert the final <unk> token
      assert(lastKey == bpeConfig.vocabConfig.unkToken)
    }
  }

  describe("getStats") {
    it("count adjacent pairs correctly") {
      val tokens = Seq(1, 2, 3, 1, 2)
      val stats = getStats(tokens)

      // Check that the pairs are correct
      assert(stats.contains((1, 2)))
      assert(stats.contains((2, 3)))
      assert(stats((1, 2)) == 2) // appears twice
      assert(stats((2, 3)) == 1) // appears once
    }

    it("return pairs sorted in descending order by default") {
      val tokens = Seq(1, 2, 1, 2, 3)
      val stats = getStats(tokens)

      // The first pair should be the most frequent
      val mostFrequentPair = stats.head._1
      assert(mostFrequentPair == (1, 2))
    }

    it("sort pairs in ascending order if descending=false") {
      val tokens = Seq(1, 2, 1, 2, 3)
      val stats = getStats(tokens, descending = false)

      // The first pair should be the least frequent
      val leastFrequentPair = stats.head._1
      assert(leastFrequentPair == (2, 1))
    }

    it("handle empty input") {
      val tokens = Seq.empty[Int]
      val stats = getStats(tokens)
      assert(stats.isEmpty) // no pairs in empty sequence
    }

    it("handle a single token input") {
      val tokens = Seq(42)
      val stats = getStats(tokens)
      assert(stats.isEmpty) // no adjacent pairs possible
    }
  }
}
