# miKrate

_Flexible migrations for Kotlin_
<br>
[![platform: gitlab](https://img.shields.io/badge/platform-gitlab-%23fca121?logo=gitlab)](https://gitlab.com/factory-org/tools/mikrate)
[![language: kotlin](https://img.shields.io/badge/language-kotlin-%230095d5?logo=kotlin)](https://kotlinlang.org/)
[![dialect: sqlite](https://img.shields.io/badge/dialect-sqlite-%23003b57?logo=sqlite)](https://www.sqlite.org)
[![dialect: postgres](https://img.shields.io/badge/dialect-postgres-%23336791?logo=postgresql)](https://www.postgresql.org/)
[![docs: mkdocs](https://img.shields.io/badge/docs-mkdocs-%23000000?logo=markdown&logoColor=%23000000)](https://factory-org.gitlab.io/tools/mikrate/)
[![API docs: dokka](https://img.shields.io/badge/API%20docs-dokka-%23f8873c?logo=kotlin&logoColor=%23f8873c)](https://mikrate-docs.web.app)

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

## Supported Platform

Currently, only the JVM platform is supported.

## WIP

- Complete documentation
- Better locking
- Validation
- Auto-generated migrations (with pluggable ORMs, a safe code generator (possibly
  via [kotlinpoet](https://square.github.io/kotlinpoet/)) and a gradle plugin)
