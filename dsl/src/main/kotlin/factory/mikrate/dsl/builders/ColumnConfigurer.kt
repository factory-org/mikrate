package factory.mikrate.dsl.builders

import factory.mikrate.dsl.MigrationDsl
import factory.mikrate.dsl.helpers.ColumnRef

/**
 * Configuration of new columns.
 *
 * @see CreateTableBuilder.column
 */
@MigrationDsl
public class ColumnConfigurer(tableName: String, columnName: String) {
    private var unique = false
    private var uniqueName = "uix_${tableName}_$columnName"
    private var foreignKeyRef: ColumnRef? = null
    private var foreignKeyName: String? = null
    private var primary = false
    private var nullable = false

    /**
     * Marks the column as part of the primary key.
     */
    public fun primary() {
        primary = true
    }

    /**
     * Marks the column not as part of the primary key.
     */
    public fun notPrimary() {
        primary = false
    }

    /**
     * Marks the column as nullable
     */
    public fun nullable() {
        nullable = true
    }

    /**
     * Marks the column as non-nullable
     */
    public fun nonNullable() {
        nullable = false
    }

    /**
     * Marks the column as unique.
     *
     * @param name sets the name of the unique constraint (default: `uix_<tableName>_<columnName>`)
     */
    public fun unique(name: String = uniqueName) {
        unique = true
        uniqueName = name
    }

    /**
     * Marks the column as not unique.
     */
    public fun notUnique() {
        unique = false
    }

    /**
     * Marks the column as foreign key.
     */
    public fun referencesForeign(columnRef: ColumnRef, name: String? = null) {
        foreignKeyRef = columnRef
        foreignKeyName = name
    }

    /**
     * Marks the column as not a foreign key.
     */
    public fun noForeignReference() {
        foreignKeyRef = null
    }

    /**
     * @suppress
     */
    public fun config(): ColumnConfig {
        return ColumnConfig(primary, nullable, unique, uniqueName, foreignKeyRef, foreignKeyName)
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
        val uniqueName: String,
        /**
         * Reference to the foreign column.
         */
        val foreignKeyRef: ColumnRef?,
        /**
         * Name of the foreign key constraint.
         */
        val foreignKeyName: String?
    )
}
