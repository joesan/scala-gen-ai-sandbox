package com.genai.sandbox

import com.typesafe.config.{Config, ConfigFactory}
import scala.jdk.CollectionConverters._ // For Java-to-Scala conversions if needed

object BpeConfigLoader {

  def load(): BpeConfig = {
    val config: Config = ConfigFactory.load().getConfig("bpe")

    // Load nested configs
    val vocabCfg = config.getConfig("vocab")
    val tokenizationCfg = config.getConfig("tokenization")
    val filesCfg = config.getConfig("files")

    BpeConfig(
      vocab = VocabConfig(
        maxSize = vocabCfg.getInt("max-size"),
        maxMerges = vocabCfg.getInt("max-merges")
      ),
      inputChars = config.getString("input-chars"),
      tokenization = TokenizationConfig(
        minPairFrequency = tokenizationCfg.getInt("min-pair-frequency"),
        includeWhitespace = tokenizationCfg.getBoolean("include-whitespace")
      ),
      files = FilesConfig(
        vocabFile = filesCfg.getString("vocab-file"),
        mergesFile = filesCfg.getString("merges-file")
      )
    )
  }
}
