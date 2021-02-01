package factory.mikrate.core

import factory.mikrate.dialects.api.CoreDialect

public class Migration(
    public val id: String,
    private val upActions: List<MigrateAction>,
    private val downActions: List<MigrateAction>
) {
    public val after: MutableSet<Migration> = mutableSetOf()

    public fun upStatement(dialect: CoreDialect): String {
        return upActions.mapNotNull { it.generateStatement(dialect) }.joinToString("\n")
    }

    public fun downStatement(dialect: CoreDialect): String {
        return downActions.mapNotNull { it.generateStatement(dialect) }.joinToString("\n")
    }
}
