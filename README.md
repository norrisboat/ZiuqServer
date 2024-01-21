# Ziuq Server

Yes, Ziuq is Quiz spelled backward 😀. This is a quiz server backend implementation with [Ktor](https://ktor.io). An example usage is the [Ziuq Mobile App]() built with [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html).


[![Build](https://github.com/xxfast/NYTimes-KMP/actions/workflows/build.yml/badge.svg)](https://github.com/xxfast/NYTimes-KMP/actions/workflows/build.yml)


## ⚙️ Architecture
The database of choice is Postgres. [Koin](https://insert-koin.io/) is used for dependency injection to wire things easily. [Exposed](https://github.com/JetBrains/Exposed) is used as the SQL framework.

## 📱Features

* User authentication
* Solo Quiz
* Live Quiz using websockets

## 💾 Installation

1. Clone the repository.
2. Open the project in Android Studio.
3. Use database.sql as a guide to setup database.
4. Replace Config variables({{port}},{{db_url}},{{username}},{{password}}).
5. Build and run the app.


## ❤️ Built with
- [Koin](https://insert-koin.io/) - The pragmatic Kotlin & Kotlin Multiplatform Dependency Injection framework.
- [Kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) - KotlinX multiplatform date/time library.
- [Kotlinx-serilization](https://github.com/Kotlin/kotlinx.serialization) - Kotlin multiplatform / multi-format serialization.
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org) - An Open source JDBC driver written in Pure Java (Type 4), which communicates using the PostgreSQL native network protocol
- [Exposed](https://github.com/JetBrains/Exposed) - Kotlin SQL Framework
- [HikariCP](https://github.com/brettwooldridge/HikariCP) - A solid, high-performance, JDBC connection pool at last
- [jBCrypt](https://www.mindrot.org/projects/jBCrypt/) - implementation of OpenBSD's Blowfish password hashing code


## 🙋🏻‍♂️ Say Hi

[![twitter](https://img.shields.io/badge/twitter-@norrisboat-orange.svg?style=flat-square)](https://twitter.com/norrisboat)


## 📜 License

This project is licensed under the [MIT License](https://www.droidcon.com/2023/09/06/migrating-an-android-app-to-ios-with-kmp-part-i-first-steps-and-architecture/).