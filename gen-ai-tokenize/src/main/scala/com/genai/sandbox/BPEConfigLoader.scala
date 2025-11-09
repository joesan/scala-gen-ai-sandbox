package com.genai.sandbox

import com.typesafe.config.{Config, ConfigFactory}


object BPEConfigLoader {

  def load(env: String = "application.conf"): BpeConfig = {
    val config: Config = ConfigFactory.load(env).getConfig("bpe")

    val vocabCfg = config.getConfig("vocab")
    val tokenizationCfg = config.getConfig("tokenization")
    val filesCfg = config.getConfig("files")

    BpeConfig(
      vocabConfig = VocabConfig(
        maxSize = vocabCfg.getInt("max-size"),
        maxMerges = vocabCfg.getInt("max-merges"),
        mergeSeperator = vocabCfg.getString("merge-separator"),
        unkToken = vocabCfg.getString("unk-token")
      ),
      inputChars = Option(config.getString("input-chars")).map(_.toCharArray.toSeq),
      tokenization = TokenizationConfig(
        minPairFrequency = tokenizationCfg.getInt("min-pair-frequency"),
        includeWhitespace = tokenizationCfg.getBoolean("include-whitespace")
      ),
      files = FilesConfig(
        vocabFile = filesCfg.getString("vocab-file"),
        mergesFile = filesCfg.getString("merges-file"),
        inputFile = filesCfg.getString("input-file")
      )
    )
  }
}
