# Basic

This guide is all about learning the basics: Creating migrations and applying them via auto migrate.

## Creating a migration

In the fist place you need a migration that can be applied. Take this example:

```kotlin
// Create a migration with the id "firstMigration".
val firstMigration by migration {
    // Define what steps to take to apply the migration
    up {
        // We want to create a new table in this example
        createTable("FirstTable") {
            // We want an Int based primary key
            column("id", IntegerType) {
                // Mark it as primary key
                primary()
            }
        }
    }
}
```

Alternatively to the above method you can also set the id using a string:

```kotlin
val migration = migration("firstMigration") {
    // ...
}
```

!!! note
    An important thing to understand here is that despite the DSL, the migration at its core is just an object of the class [Migration](/tools/mikrate/api/core/core/factory.mikrate.core/-migration/index.html).
    You can put it into lists, filter it of apply custom logic to it if your use-case demands it.

## Applying the migration

While you can customize the application of migrations and orchestrate those on your own, miKrate can help you get started (and in most cases this should be the only thing you need) using auto migrate.
Auto migrate uses a helper table to record which migrations already have been applied to your database, this ensures that only migrations that are actually needed will be run.

To enable support for multiple means of accessing the database, miKrate operates on the concept of [Executors](/tools/mikrate/api/executors/executor-api/executors.executor-api/factory.mikrate.executors.api/-migration-executor/index.html).
Currently supported are the JVM built-in JDBC and the synchronous R2DBC.

For this guide we'll use the sqlite JDBC driver for simplicity:

```kotlin
// Get a connection to an in-memory sqlite database
val connection = DriverManager.getConnection("jdbc:sqlite::memory:")
// Using this JDBC connection we can now create the executor
val executor = JDBCExecutor(connection)
// Now we can create the migrator using the migrator and the dialects...
val migrator = Migrator(exec, SqliteAutoDialect, SqliteCoreDialect)
// ...and apply the migration
migrator.migrateTo(simple)
```

!!! note
    The `migrateTo` method is marked as a `suspend` function. Despite that, it can only run asynchronous if an executor is use that can execute statements asynchronously (This can be checked via [`isBlocking`](/tools/mikrate/api/executors/executor-api/executors.executor-api/factory.mikrate.executors.api/-migration-executor/is-blocking.html)).
    If you aren't in an asynchronous context, and your driver doesn't support it or you don't need it to be asynchronous, you can use [`runBlocking`](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/run-blocking.html) to run the method in a blocking manner.

## Wrapping it up

These few lines of code already give you most of what you should need to write migrations using miKrate.

```kotlin
val firstMigration by migration {
    up {
        createTable("FirstTable") {
            column("id", IntegerType) {
                primary()
            }
        }
    }
}

val connection = DriverManager.getConnection("jdbc:sqlite::memory:")
val executor = JDBCExecutor(connection)
val migrator = Migrator(exec, SqliteAutoDialect, SqliteCoreDialect)
migrator.migrateTo(simple)
```

For further reading you might want to check out the [packages you might need to implement this sample](../packages/index.md), [further documentation on the DSL](../dsl/index.md) or some of the [framework integrations](../features/frameworks.md).
