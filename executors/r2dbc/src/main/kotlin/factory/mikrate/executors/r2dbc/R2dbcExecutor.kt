package factory.mikrate.executors.r2dbc

import factory.mikrate.dialects.api.AutoMigrateDialect
import factory.mikrate.executors.api.LogRow
import factory.mikrate.executors.api.MigrationExecutor
import factory.mikrate.executors.api.MigrationResult
import io.r2dbc.spi.Connection
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.reactive.awaitSingle
import java.time.Instant
import java.util.function.BiFunction

public class R2dbcExecutor(public val connection: Connection) : MigrationExecutor {
    override val isBlocking: Boolean = false

    override suspend fun executeStatement(sql: String): MigrationResult {
        connection.createStatement(sql).execute().awaitLast()
        return MigrationResult.Success
    }

    override suspend fun listAppliedMigrations(dialect: AutoMigrateDialect): List<LogRow> {
        val statement = connection.createStatement(dialect.queryMigrationLog())
        return statement.execute().asFlow().map { it.map(logTransformer).awaitSingle() }.toList()
    }

    override fun close() {
        try {
            connection.rollbackTransaction()
        } finally {
            connection.close()
        }
    }

    private companion object {
        private val logTransformer: BiFunction<Row, RowMetadata, LogRow> = BiFunction { r, _ ->
            LogRow(r["id", ByteArray::class.java], r["timestamp", Instant::class.java])
        }
    }
}
