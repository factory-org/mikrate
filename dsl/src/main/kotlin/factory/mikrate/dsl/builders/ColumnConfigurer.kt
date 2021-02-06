package factory.mikrate.dsl.builders

import factory.mikrate.dsl.MigrationDsl

/**
 * Configuration of new columns.
 *
 * @see CreateTableBuilder.column
 */
@MigrationDsl
public class ColumnConfigurer(tableName: String, columnName: String) {
    private var unique = false
    private var uniqueName = "uix_${tableName}_$columnName"
    private var primary = false
    private var nullable = false

    /**
     * Marks the column as part of the primary key.
     *
     * @param value pass `false` to invert behavior
     */
    public fun primary(value: Boolean = true) {
        primary = value
    }

    /**
     * Marks the column as nullable
     *
     * @param value pass `false` to invert behavior
     */
    public fun nullable(value: Boolean = true) {
        nullable = value
    }

    /**
     * Marks a column as unique.
     *
     * @param name sets the name of the unique constraint (default: `uix_<tableName>_<columnName>`)
     * @param value pass `false` to invert behavior
     */
    public fun unique(name: String = uniqueName, value: Boolean = true) {
        unique = value
        uniqueName = name
    }

    /**
     * @suppress
     */
    public fun config(): ColumnConfig {
        return ColumnConfig(primary, nullable, unique, uniqueName)
    }

    /**
     * Stores the configuration for a new column.
     */
    public data class ColumnConfig(
        /**
         * Whether the column is part of the primary key.
         */
        val primary: Boolean,
        /**
         * Whether the column is nullable
         */
        val nullable: Boolean,
        /**
         * Whether the column is nullable
         */
        val unique: Boolean,
        /**
         * The name of the unique constraint, if applicable.
         */
        val uniqueName: String
    )
}
