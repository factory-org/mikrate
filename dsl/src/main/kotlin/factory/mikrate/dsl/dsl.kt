package factory.mikrate.dsl

import factory.mikrate.core.Migration
import factory.mikrate.dsl.builders.MigrationBuilder
import kotlin.properties.ReadOnlyProperty

/**
 * Builder for a new [Migration].
 */
public fun migration(id: String, block: MigrationBuilder.() -> Unit): Migration {
    val builder = MigrationBuilder(id)
    block(builder)
    return builder.build()
}

/**
 * Builder for a new [Migration].
 *
 * The id of the migration corresponds to the property name.
 *
 * ```kotlin
 * val migrationId by migration {
 *     [...]
 * }
 * ```
 */
public fun migration(block: MigrationBuilder.() -> Unit): ReadOnlyProperty<Nothing?, Migration> {
    return ReadOnlyProperty { _, property ->
        val builder = MigrationBuilder(property.name)
        block(builder)
        builder.build()
    }
}
