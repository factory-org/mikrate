package factory.mikrate.dsl

import factory.mikrate.core.Migration
import factory.mikrate.dsl.builders.MigrationBuilder
import kotlin.properties.ReadOnlyProperty

public fun migration(id: String, block: MigrationBuilder.() -> Unit): Migration {
    val builder = MigrationBuilder(id)
    block(builder)
    return builder.build()
}

public fun migration(block: MigrationBuilder.() -> Unit): ReadOnlyProperty<Nothing?, Migration> {
    return ReadOnlyProperty { _, property ->
        val builder = MigrationBuilder(property.name)
        block(builder)
        builder.build()
    }
}
