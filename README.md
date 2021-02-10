# miKrate

_Flexible migrations for Kotlin_
<br>
[![platform: gitlab](https://img.shields.io/badge/platform-gitlab-%23fca121?logo=gitlab)](https://gitlab.com/factory-org/tools/mikrate)
[![language: kotlin](https://img.shields.io/badge/language-kotlin-%230095d5?logo=kotlin)](https://kotlinlang.org/)
[![dialect: sqlite](https://img.shields.io/badge/dialect-sqlite-%23003b57?logo=sqlite)](https://www.sqlite.org)
[![dialect: postgres](https://img.shields.io/badge/dialect-postgres-%23336791?logo=postgresql)](https://www.postgresql.org/)
[![docs: mkdocs](https://img.shields.io/badge/docs-mkdocs-%23000000?logo=markdown&logoColor=%23000000)](https://factory-org.gitlab.io/tools/mikrate/)
[![API docs: dokka](https://img.shields.io/badge/API%20docs-dokka-%23f8873c?logo=kotlin&logoColor=%23f8873c)](https://factory-org.gitlab.io/tools/mikrate/api/)

🚧 This is still very much work in progress so don't expect all APIs to be stable 🚧

miKrate aims at providing an easy, typesafe, pluggable DSL and API to allow you to quickly develop migrations, apply
them automatically and all of that database agnostic (dialects are pluggable).

## Sample

The sample creates a table with

```kotlin
val sampleMigration by migration {
    up {
        createTable("SampleTable") {
            column("id", IntegerType) {
                primary()
            }
            column("string", VarcharType(32))
        }
    }
}


val exec = JDBCExecutor("jdbc:sqlite:sample.db")
val mig = Migrator(exec, SqliteAutoDialect, SqliteCoreDialect)

mig.migrateTo(sampleMigration)
```

## Components

### [Core](./core)

Contains the core classes and other glue to hold most things together.

### [DSL](./dsl)

Provides a DSL for creating migrations.

### [Auto-Migrate](./auto-migrate)

Provides functions to automatically apply the migrations to a database.

### [Executors](./executors)

Provides various executors for various standards of connecting to the database.
See [here](./executors/README.md#supported-executors) for a list of executors supported.

### [Dialects](./dialects)

Provides dialects for various databases. See [here](./dialects/README.md#supported-dialects) for a list of dialects
supported.

## Supported Platform

Currently, only the JVM platform is supported, but in general it should be possible to convert it into a multiplatform
project.

## WIP

- Documentation
- Better locking
- Validation
- Auto-generated migrations (with pluggable ORMs, an API safe code generation (via library) and gradle plugin)
