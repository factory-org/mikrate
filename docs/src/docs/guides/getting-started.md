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

The important thing to understand here is that despite all the DSL the migration at its core ist just an object of the
class [Migration](/tools/mikrate/api/core/core/factory.mikrate.core/-migration/index.html). You can put it into lists,
filter it of apply custom logic to it if your use-case demands it.
