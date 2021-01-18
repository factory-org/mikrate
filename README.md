# Mikrate

Flexible migrations for Kotlin.

## Projects

### [core](./core)

Contains the core classes, the SQL generation and most other things needed to create the migrations.

### [dsl](./dsl)

Provides a DSL for creating migrations.

### [auto-migrate](./auto-migrate)

Provides functions to automatically apply the migrations to a database.

### [executor-api](./executors/api)

Provides the executor API.

### [executor-jdbc](./executors/jdbc)

Provides an executor for JDBC.

### [executor-r2dbc](./executors/r2dbc)

Provides an executor for R2DBC.

## Databases

Legend:

- ✅ = Full
- ↗ = External / 3rd party
- 🚧 = Partial support
- ⭕ = Not (yet) supported
- ⛔ = Intentionally unsupported

| DBMS       | Core Support | Auto Migrate | Full JDBC Support | Full R2DBC Support | Comments |
| ---------- | :----------: | :----------: | :---------------: | :----------------: | -------- |
| SQLite     | ✅            | ✅            | ✅                 | ⭕<br>No Driver     |          |
| PostgreSQL | ✅            | ✅            | ✅                 | ✅                  |          |

It is currently not possible to easily expand this to support other databases (e.g. via plugins), this is planned for
the future.

## Supported Platform

Currently, only the JVM platform is supported, but in general it should be possible to convert it into a multiplatform
project.
