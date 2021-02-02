# miKrate

Flexible migrations for Kotlin.

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
