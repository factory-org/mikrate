package factory.mikrate.dsl.builders

import factory.mikrate.dsl.MigrationDsl

@MigrationDsl
public class ColumnConfigurer(tableName: String, columnName: String) {
    private var unique = false
    private var uniqueName = "uix_${tableName}_$columnName"
    private var primary = false
    private var nullable = false

    public fun primary(value: Boolean = true) {
        primary = value
    }

    public fun nullable(value: Boolean = true) {
        nullable = value
    }

    public fun unique(value: Boolean = true, name: String = uniqueName) {
        unique = value
        uniqueName = name
    }

    public fun config(): ColumnConfig {
        return ColumnConfig(primary, nullable, unique, uniqueName)
    }

    public data class ColumnConfig(
        val primary: Boolean,
        val nullable: Boolean,
        val unique: Boolean,
        val uniqueName: String
    )
}
