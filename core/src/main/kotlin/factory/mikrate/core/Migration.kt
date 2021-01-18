package factory.mikrate.core

public class Migration(
    public val id: String,
    private val upActions: List<MigrateAction>,
    private val downActions: List<MigrateAction>
) {
    public val after: MutableSet<Migration> = mutableSetOf()

    public fun upStatement(dialect: Dialect): String {
        return upActions.mapNotNull { it.generateStatement(dialect) }.joinToString("\n")
    }

    public fun downStatement(dialect: Dialect): String {
        return downActions.mapNotNull { it.generateStatement(dialect) }.joinToString("\n")
    }
}
