package factory.mikrate.executors.jdbc

import factory.mikrate.dialects.api.AutoMigrateDialect
import factory.mikrate.executors.api.LogRow
import factory.mikrate.executors.api.MigrationExecutor
import factory.mikrate.executors.api.MigrationResult
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

public class JDBCExecutor(private val connection: Connection) :
    MigrationExecutor {

    public constructor(
        connectionString: String,
        connectionProps: Properties = Properties()
    ) : this(DriverManager.getConnection(connectionString, connectionProps))

    override val isBlocking: Boolean = true

    override suspend fun executeStatement(sql: String): MigrationResult {
        val statement = connection.createStatement()
        statement.use {
            statement.executeUpdate(sql)
        }
        return MigrationResult.Success
    }

    override suspend fun listAppliedMigrations(dialect: AutoMigrateDialect): List<LogRow> {
        val statement = connection.createStatement()
        val list = mutableListOf<LogRow>()
        statement.use {
            val res = statement.executeQuery(dialect.queryMigrationLog())
            while (res.next()) {
                list.add(LogRow(res.getBytes("id"), res.getTimestamp("timestamp").toInstant()))
            }
        }
        return list
    }

    override fun close() {
        connection.close()
    }
}
