# Project Artimus

Project Artimus is a Java-based application implementing core features for the Immortals UME platform. This repository contains source code, low-level design docs, build configuration for development and CI, and a curated set of learning problems.

Mission

This project purposefully constructs problems and exercises covering foundational first-year college topics and progressively harder interview-style challenges aimed at preparing learners to apply for FAANG-level roles. Problems include algorithmic practice, system design mini-tasks, low-level design exercises, and real-world engineering scenarios.

Quickstart

- Prerequisites: JDK 17+, Maven or Gradle (project uses Maven by default)
- Build: mvn clean package
- Run tests: mvn test
- IDE: Open the project root in IntelliJ IDEA or Eclipse

Repository layout

- src/main/java - application source code
- src/test/java - unit and integration tests
- src/main/java/com/immortals/LLD - Low Level Design docs and diagrams

Contributing

- Fork the repository and open a pull request for changes.
- Update LLD docs under src/main/java/com/immortals/LLD when changing architecture or public APIs.
- Include tests for behavioral changes and run mvn test locally before opening PR.

Contacts & Maintainers

Refer to CODEOWNERS or the repository root README on the host for maintainer information.

License

This project follows the license declared at the repository root (check LICENSE file).
