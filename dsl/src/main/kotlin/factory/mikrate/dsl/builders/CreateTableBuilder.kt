package factory.mikrate.dsl.builders

import factory.mikrate.core.DbType
import factory.mikrate.core.actions.CreateTableAction
import factory.mikrate.dsl.MigrationDsl
import factory.mikrate.dsl.helpers.ColumnRef

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
    private val columns: MutableMap<String, CreateTableAction.NewColumn> = mutableMapOf()

    /**
     * A list of constraints of the new table.
     */
    private val constraints: MutableMap<String, CreateTableAction.Constraint> = mutableMapOf()

    /**
     * Adds a new column to the table.
     *
     * @param name Name of the column
     * @param type Type of the column
     * @param nullable Whether the column is nullable
     */
    public fun column(name: String, type: DbType, nullable: Boolean = false): ColumnRef {
        columns[name] = CreateTableAction.NewColumn(type, nullable, null)
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
        columns[name] = CreateTableAction.NewColumn(type, config.nullable, unique)
        return ColumnRef(tableName, name)
    }

    /**
     * Defines a new unique constraint with custom name on the table.
     *
     * @param name Name of the constraint
     * @param columns Columns that are part of the constraint
     */
    public fun unique(name: String, vararg columns: ColumnRef) {
        constraints[name] = CreateTableAction.Constraint.UniqueConstraint(columns.map { it.column })
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
     * @suppress
     */
    public fun build(): CreateTableAction {
        return CreateTableAction(tableName, columns, constraints)
    }
}
