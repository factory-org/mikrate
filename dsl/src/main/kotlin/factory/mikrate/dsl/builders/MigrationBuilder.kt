package factory.mikrate.dsl.builders

import factory.mikrate.core.MigrateAction
import factory.mikrate.core.Migration
import factory.mikrate.dsl.MigrationDsl

@MigrationDsl
public class MigrationBuilder(public val id: String) {
    private var upActions: List<MigrateAction> = listOf()
    private var downActions: List<MigrateAction> = listOf()
    private val after: MutableList<Migration> = mutableListOf()

    public fun after(vararg migrations: Migration) {
        after.addAll(migrations)
    }

    public fun up(block: ActionBuilder.() -> Unit) {
        val builder = ActionBuilder()
        block(builder)
        upActions = builder.actions
    }

    public fun down(block: ActionBuilder.() -> Unit) {
        val builder = ActionBuilder()
        block(builder)
        downActions = builder.actions
    }

    public fun build(): Migration {
        return Migration(id, upActions, downActions)
    }
}
