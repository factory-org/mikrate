package factory.mikrate.executors.jdbc

import factory.mikrate.dialects.api.AutoMigrateSqlGen
import factory.mikrate.executors.api.LogRow
import factory.mikrate.executors.api.MigrationExecutor
import factory.mikrate.executors.api.MigrationResult
import java.sql.DriverManager
import java.util.*

public class JDBCExecutor(private val connectionString: String, public val connectionProps: Properties) :
    MigrationExecutor {
    private val connection = DriverManager.getConnection(connectionString, connectionProps)

    override val isBlocking: Boolean = true

    override suspend fun executeStatement(sql: String): MigrationResult {
        val statement = connection.createStatement()
        statement.use {
            statement.execute(sql)
        }
        return MigrationResult.Success
    }

    override suspend fun listAppliedMigrations(dialect: AutoMigrateSqlGen): List<LogRow> {
        TODO("Not yet implemented")
    }

    override fun close() {
        connection.close()
    }
}
