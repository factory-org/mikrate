package factory.mikrate.core

public interface MigrateAction {
    /**
     * If it returns `null`, nothing will be executed.
     */
    public fun generateStatement(dialect: Dialect): String?
}
