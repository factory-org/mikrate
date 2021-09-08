package factory.mikrate.dsl.builders

import factory.mikrate.core.DbType
import factory.mikrate.core.actions.CreateTableAction
import factory.mikrate.dialects.api.models.NewTable
import factory.mikrate.dsl.MigrationDsl
import factory.mikrate.dsl.helpers.ColumnRef
import factory.mikrate.dsl.helpers.TableRef

/**
 * Defines a new table to be created.
 */
@MigrationDsl
public class CreateTableBuilder(
    /**
     * The name of the table to build.
     */
    public val tableName: String
) {
    /**
     * A list of all columns of the new table.
     */
    private val columns: MutableMap<String, NewTable.Column> = mutableMapOf()

    /**
     * A list of constraints of the new table.
     */
    private val constraints: MutableMap<String, NewTable.Constraint> = mutableMapOf()

    /**
     * Adds a new column to the table.
     *
     * @param name Name of the column
     * @param type Type of the column
     * @param nullable Whether the column is nullable
     */
    public fun column(name: String, type: DbType, nullable: Boolean = false): ColumnRef {
        columns[name] = NewTable.Column(type.dialectDbType, nullable, null, null)
        return ColumnRef(tableName, name)
    }

    /**
     * Adds a new column with extended configuration to the table.
     *
     * @param name Name of the column
     * @param type Type of the column
     * @param block Extended configuration of the column
     */
    public fun column(name: String, type: DbType, block: ColumnConfigurer.() -> Unit): ColumnRef {
        val configurer = ColumnConfigurer(tableName, name)
        block(configurer)
        val config = configurer.config()
        val unique = if (config.unique) config.uniqueName else null
        val foreign = if (config.foreignKeyRef == null) {
            null
        } else {
            val constraintName =
                config.foreignKeyName
                    ?: "fk_${tableName}_${name}_${config.foreignKeyRef.table}_${config.foreignKeyRef.column}"
            NewTable.Column.ForeignColumnConfig(
                constraintName,
                config.foreignKeyRef.table,
                config.foreignKeyRef.table
            )
        }
        columns[name] = NewTable.Column(type.dialectDbType, config.nullable, unique, foreign)
        return ColumnRef(tableName, name)
    }

    /**
     * Defines a new unique constraint with custom name on the table.
     *
     * @param name Name of the constraint
     * @param columns Columns that are part of the constraint
     */
    public fun unique(name: String, vararg columns: String) {
        constraints[name] = NewTable.Constraint.UniqueConstraint(columns.toList())
    }

    /**
     * Defines a new unique constraint with automatic name on the table.
     *
     * The name will be generated with the following schema:
     * `uid_<table name>_<column 1 name>_<column 2 name>_..._<column n name>`
     *
     * @param columns Columns that are part of the constraint
     */
    public fun unique(vararg columns: String) {
        val name = "uix_${tableName}_${columns.joinToString("_")}"
        unique(name, columns = columns)
    }

    /**
     * Defines a new unique constraint with custom name on the table.
     *
     * @param name Name of the constraint
     * @param columns Columns that are part of the constraint
     */
    public fun unique(name: String, vararg columns: ColumnRef) {
        require(columns.all { it.table == tableName }) { "Unique columns have to be in the same table" }
        constraints[name] = NewTable.Constraint.UniqueConstraint(columns.map { it.column })
    }

    /**
     * Defines a new unique constraint with automatic name on the table.
     *
     * The name will be generated with the following schema:
     * `uid_<table name>_<column 1 name>_<column 2 name>_..._<column n name>`
     *
     * @param columns Columns that are part of the constraint
     */
    public fun unique(vararg columns: ColumnRef) {
        val name = "uix_${tableName}_${columns.joinToString("_") { it.column }}"
        unique(name, columns = columns)
    }

    /**
     * Defines a new foreign key constraint with custom name on the table.
     *
     * @param name Name of the constraint
     * @param columnMapping Mapping which local columns are mapped to which columns on the foreign table
     */
    public fun foreignKeys(name: String, foreignTable: TableRef, vararg columnMapping: Pair<String, String>) {
        require(columnMapping.isNotEmpty()) { "Column mapping may not be empty" }
        constraints[name] = NewTable.Constraint.ForeignKey(foreignTable.tableName, columnMapping.toMap())
    }

    /**
     * Defines a new foreign key constraint with automatic name on the table.
     *
     * The name will be generated with the following schema:
     * `fk_<table name>_<column 1 name>_..._<column n name>_<foreign table name>_<foreign column 1 name>_..._<foreign column n name>`
     *
     * @param columnMapping Mapping which local columns are mapped to which columns on the foreign table
     */
    public fun foreignKeys(foreignTable: TableRef, vararg columnMapping: Pair<String, String>) {
        require(columnMapping.isNotEmpty()) { "Column mapping may not be empty" }
        val localColumns = columnMapping.joinToString("_") { it.first }
        val foreignColumns = columnMapping.joinToString("_") { it.second }
        val name = "uix_${tableName}_${localColumns}_${foreignTable.tableName}_${foreignColumns}"
        foreignKeys(name, foreignTable, columnMapping = columnMapping)
    }

    /**
     * @suppress
     */
    public fun build(): CreateTableAction {
        return CreateTableAction(tableName, columns, constraints)
    }
}
