package factory.mikrate.dsl.contexts

import factory.mikrate.core.MigrateAction
import factory.mikrate.dsl.MigrationDsl

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
}
