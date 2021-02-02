package factory.mikrate.automigrate

import factory.mikrate.core.Migration
import factory.mikrate.dialects.api.AutoMigrateDialect
import factory.mikrate.executors.api.LogRow
import factory.mikrate.executors.api.MigrationExecutor
import java.time.Instant

public class Migrator(private val executor: MigrationExecutor, private val dialect: AutoMigrateDialect) {
    public suspend fun migrateTo(migration: Migration) {
        val appliedMigrations = executor.listAppliedMigrations(dialect.autoMigrate)
        val requiredMigrations = resolveRequiredMigrations(appliedMigrations, migration)
        for (current in requiredMigrations) {
            val up = current.upStatement(dialect)
            val stmt = listOf(
                dialect.autoMigrate.transactionStart(),
                up,
                dialect.autoMigrate.insertMigrationIntoLog(hashMigrationId(current.id), Instant.now()),
                dialect.autoMigrate.transactionCommit()
            ).joinToString("\n\n")
            executor.executeStatement(stmt)
        }
    }

    private fun resolveRequiredMigrations(appliedMigrations: List<LogRow>, rootMigration: Migration): List<Migration> {
        val appliedIds = appliedMigrations.map { it.id }

        val migrations = mutableListOf<Migration>()
        val nodeList = mutableListOf(rootMigration)
        while (nodeList.isNotEmpty()) {
            val item = nodeList.first()
            nodeList.removeFirst()
            if (appliedIds.contains(hashMigrationId(item.id))) {
                continue
            }
            nodeList.addAll(0, item.after)
            migrations.add(0, item)
        }
        return migrations
    }
}
