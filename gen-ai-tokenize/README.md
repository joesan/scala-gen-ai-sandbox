# Scala Byte Pair Encoding (BPE) Project

This project implements a **Byte Pair Encoding (BPE) tokenizer and decoder** in Scala. It provides classes to **tokenize text, encode sequences, merge token pairs, and decode them back to text**. The project is fully configurable via a `.conf` file and includes unit tests and a sample GitHub Actions CI workflow.

---

## Features

- Build a character-level vocabulary from input characters.
- Encode text into token IDs using BPE merges.
- Decode token IDs back to text.
- Configurable input characters, merge rules, and special tokens (like `<unk>`).
- Fully unit-tested using ScalaTest.
- CI/CD integration using GitHub Actions.

---

## Usage
Running the Main App

Clone the repository and navigate to the project directory. Ensure you have [sbt](https://www.scala-sbt.org/) installed.

```bash
sbt "runMain com.genai.sandbox.BPEApp"
```

The app will:

- Load configuration from application.conf.
- Build the input vocabulary.
 -Encode the sample input text using BPE merges.
 -Decode the encoded tokens back to text.
- Print results to the console.

## Running Tests

```bash
sbt test
```

Unit tests cover:

 - Tokenizer: builds vocab and maps text to token IDs.
 - Encoder: merges pairs and updates vocab.
 - Decoder: converts token IDs back to text, ensuring round-trip correctness.

## License

MIT License © 2025 https://goithub.com/joesan