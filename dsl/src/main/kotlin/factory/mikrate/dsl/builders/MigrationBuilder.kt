package factory.mikrate.dsl.builders

import factory.mikrate.core.MigrateAction
import factory.mikrate.core.Migration
import factory.mikrate.dsl.MigrationDsl

/**
 * Creates and configures a migration.
 *
 * @see Migration
 */
@MigrationDsl
public class MigrationBuilder(
    /**
     * Sets the unique id of the migration. No other migration may have the same id.
     */
    public val id: String
) {
    private var upActions: List<MigrateAction> = listOf()
    private var downActions: List<MigrateAction> = listOf()
    private val after: MutableList<Migration> = mutableListOf()

    /**
     * Add a migration that has to be executed before the current one is executed.
     */
    public fun dependsOn(vararg migrations: Migration) {
        after.addAll(migrations)
    }

    /**
     * Configures all stops to apply the migration.
     *
     * @see ActionBuilder
     */
    public fun up(block: ActionBuilder.() -> Unit) {
        val builder = ActionBuilder()
        block(builder)
        upActions = builder.actions
    }

    /**
     * Configures all stops to revert the migration.
     *
     * @see ActionBuilder
     */
    public fun down(block: ActionBuilder.() -> Unit) {
        val builder = ActionBuilder()
        block(builder)
        downActions = builder.actions
    }

    /**
     * @suppress
     */
    public fun build(): Migration {
        return Migration(id, upActions, downActions)
    }
}
