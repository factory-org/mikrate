package factory.mikrate.executors.r2dbc

import factory.mikrate.dialects.api.AutoMigrateDialect
import factory.mikrate.executors.api.LogRow
import factory.mikrate.executors.api.MigrationExecutor
import factory.mikrate.executors.api.MigrationResult
import io.r2dbc.spi.Connection
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import java.time.Instant
import java.util.function.BiFunction
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.runBlocking

public class R2dbcExecutor(public val connection: Connection) : MigrationExecutor {
    override val isBlocking: Boolean = false

    override suspend fun executeStatement(sql: String): MigrationResult {
        connection.createStatement(sql).execute().awaitLast()
        return MigrationResult.Success
    }

    override suspend fun listAppliedMigrations(dialect: AutoMigrateDialect): List<LogRow> {
        val statement = connection.createStatement(dialect.queryMigrationLog())
        val result = statement.execute().awaitSingle()
        return result.map(logTransformer).asFlow().toList()
    }

    override fun close() {
        runBlocking {
            try {
                connection.rollbackTransaction().awaitFirstOrNull()
            } finally {
                connection.close().awaitFirstOrNull()
            }
        }
    }

    private companion object {
        private val logTransformer: BiFunction<Row, RowMetadata, LogRow> = BiFunction { r, _ ->
            LogRow(r["id", ByteArray::class.java]!!, Instant.parse(r["timestamp", String::class.java]!!))
        }
    }
}
