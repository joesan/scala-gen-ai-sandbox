package com.genai.sandbox

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should

final class BPEConfigLoaderSpec extends AnyFlatSpec with should.Matchers with BaseSpec {
  "BPEConfigLoader.load(...)" should "load application.conf from classpath" in {
    val cfg = BPEConfigLoader.load()


    // Check main blocks exist
    assert(cfg.vocabConfig != null)
    assert(cfg.tokenization != null)
    assert(cfg.files != null)

    // ---- vocabConfig ----
    assert(cfg.vocabConfig.encoding.nonEmpty)
    assert(cfg.vocabConfig.maxMerges > 0)
    assert(cfg.vocabConfig.mergeSeperator.nonEmpty)
    assert(cfg.vocabConfig.unkToken.nonEmpty)

    // ---- inputChars optional ----
    cfg.inputChars.foreach(chars => assert(chars.nonEmpty))

    // ---- tokenization ----
    assert(cfg.tokenization.minPairFrequency >= 0)
    // boolean field just needs to exist; no assert

    // ---- files ----
    assert(cfg.files.vocabFile.nonEmpty)
    assert(cfg.files.mergesFile.nonEmpty)
    assert(cfg.files.inputFile.nonEmpty)
  }
}
