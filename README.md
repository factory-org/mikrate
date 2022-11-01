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

## Contributing

If you have any feature request, just put them on the issue tracker(s), the library isn't finished and stable yet, so
it's easier to integrate new stuff now. Issues can be filed on
[GitLab](https://gitlab.com/factory-org/tools/mikrate/-/issues) (preferred) and
[GitHub](https://github.com/factory-org/mikrate/issues), for feature request please use GitLab, so the is no split in
discussion.

If you want to implement something new or improved, free to create
[a new merge request](https://gitlab.com/factory-org/tools/mikrate/-/merge_requests) on GitLab, for larger features it
might make sense to create a feature request first, to see if and how the feature even makes sense and save your time
in case the feature is not fit for inclusion.

Current development docs can be found [here](https://mikrate-docs-dev.web.app).

## WIP

- Complete documentation
- Better locking
- Validation
- Auto-generated migrations (with pluggable ORMs, a safe code generator (possibly
  via [kotlinpoet](https://square.github.io/kotlinpoet/)) and a gradle plugin)
