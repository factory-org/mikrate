package factory.mikrate.executors.api

import factory.mikrate.dialects.api.AutoMigrateSqlGen

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
    public suspend fun listAppliedMigrations(dialect: AutoMigrateSqlGen): List<LogRow>
}
