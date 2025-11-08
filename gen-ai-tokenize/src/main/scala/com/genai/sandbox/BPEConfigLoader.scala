package com.genai.sandbox

import com.typesafe.config.{Config, ConfigFactory}


object BPEConfigLoader {

  def load(env: String = "application.conf"): BpeConfig = {
    val config: Config = ConfigFactory.load(env).getConfig("bpe")

    val vocabCfg = config.getConfig("vocab")
    val tokenizationCfg = config.getConfig("tokenization")
    val filesCfg = config.getConfig("files")

    BpeConfig(
      vocab = VocabConfig(
        maxSize = vocabCfg.getInt("max-size"),
        maxMerges = vocabCfg.getInt("max-merges")
      ),
      inputChars = config.getString("input-chars").toCharArray.toSeq,
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
