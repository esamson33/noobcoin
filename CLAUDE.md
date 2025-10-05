# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

NoobChain is a simple educational blockchain implementation in Java. It demonstrates basic blockchain concepts including block creation, hash chains, and SHA-256 cryptographic hashing.

## Build System

This project uses Maven with Java 25.

**Build the project:**
```bash
mvn clean package
```

**Run tests:**
```bash
mvn test
```

**Run a specific test:**
```bash
mvn test -Dtest=AppTest#shouldAnswerWithTrue
```

**Run the application:**
```bash
mvn exec:java -Dexec.mainClass="com.esamson.blockchain.NoobChain"
```

Or after building:
```bash
java -cp target/noobchain-0.1.0.jar com.esamson.blockchain.NoobChain
```

## Architecture

### Core Components

**Block** (`src/main/java/com/esamson/blockchain/Block.java`)
- Represents a single block in the blockchain
- Each block contains: hash, previous block's hash, data (message), and timestamp
- Hash is calculated from prevHash + timestamp + data using SHA-256
- Hash is computed automatically on block creation via `calculateHash()`

**StringUtil** (`src/main/java/com/esamson/blockchain/StringUtil.java`)
- Utility class for cryptographic operations
- `applySha256()` method converts strings to SHA-256 hashes
- Returns hexadecimal string representation of the hash

**NoobChain** (`src/main/java/com/esamson/blockchain/NoobChain.java`)
- Main entry point demonstrating blockchain creation
- Creates a chain of blocks where each block references the previous block's hash
- Genesis block (first block) has prevHash set to "0"

### Package Structure Note

There's a package naming inconsistency:
- Main code uses `com.esamson.blockchain`
- Test code uses `com.samson.blockchain`
- pom.xml declares `com.samson.blockchain`

When adding new classes, use `com.esamson.blockchain` to match the existing main codebase.

## Key Design Patterns

The blockchain is implemented as a **linked chain** where:
1. Each block stores the hash of the previous block
2. Any tampering with block data invalidates the hash chain
3. The genesis block starts the chain with prevHash = "0"
4. Hash integrity can be verified by recalculating each block's hash
