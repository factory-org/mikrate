package factory.mikrate.executors.api

public interface MigrationExecutor : AutoCloseable {
    /**
     * Specifies whether the driver supports non-blocking execution.
     */
    public val isBlocking: Boolean

    /**
     *
     */
    public suspend fun executeStatement(sql: String): MigrationResult

    /**
     *
     */
    public suspend fun listAppliedMigrations(query: String): List<LogRow>
}
