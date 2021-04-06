package factory.mikrate.dsl.contexts

import factory.mikrate.core.MigrateAction
import factory.mikrate.core.actions.RenameColumnAction
import factory.mikrate.dsl.MigrationDsl
import factory.mikrate.dsl.helpers.ColumnRef

/**
 * Enables easy access to multiple operations in the context of a specific table.
 *
 * @see factory.mikrate.dsl.builders.ActionBuilder.table
 */
@MigrationDsl
public class TableContext(
    /**
     * Name of the table on which the operations should be executed.
     */
    public val tableName: String
) {
    /**
     * List of the actions that will be executed in the table
     *
     * @see MigrateAction
     */
    public val actions: MutableList<MigrateAction> = mutableListOf()

    /**
     * Creates a reference to a column.
     */
    public fun column(name: String): ColumnRef = ColumnRef(tableName, name)

    /**
     * Renames a column.
     */
    public fun renameColumn(column: String, to: String) {
        actions.add(RenameColumnAction(tableName, column, to))
    }

    /**
     * Renames a column.
     */
    public fun ColumnRef.renameTo(to: String) {
        actions.add(RenameColumnAction(table, column, to))
    }
}
