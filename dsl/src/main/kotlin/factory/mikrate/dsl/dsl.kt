package factory.mikrate.dsl

import factory.mikrate.core.Migration
import factory.mikrate.dsl.builders.MigrationBuilder

public fun migration(id: String, block: MigrationBuilder.() -> Unit): Migration {
    val builder = MigrationBuilder(id)
    block(builder)
    return builder.build()
}
