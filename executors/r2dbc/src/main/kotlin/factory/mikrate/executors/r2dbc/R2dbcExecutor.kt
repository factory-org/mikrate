package factory.mikrate.executors.r2dbc

import factory.mikrate.executors.api.LogRow
import factory.mikrate.executors.api.MigrationExecutor
import factory.mikrate.executors.api.MigrationResult
import io.r2dbc.spi.Connection
import kotlinx.coroutines.reactive.awaitSingle

public class R2dbcExecutor(public val connection: Connection) : MigrationExecutor {
    override val isBlocking: Boolean = false

    override suspend fun executeStatement(sql: String): MigrationResult {
        connection.createStatement(sql).execute().awaitSingle()
        return MigrationResult.Success
    }

    override suspend fun listAppliedMigrations(query: String): List<LogRow> {
        TODO("Not yet implemented")
    }

    override fun close() {
        try {
            connection.rollbackTransaction()
        } finally {
            connection.close()
        }
    }
}
