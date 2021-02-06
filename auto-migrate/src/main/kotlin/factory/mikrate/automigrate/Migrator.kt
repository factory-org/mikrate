package factory.mikrate.automigrate

import factory.mikrate.core.Migration
import factory.mikrate.dialects.api.AutoMigrateDialect
import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.executors.api.LogRow
import factory.mikrate.executors.api.MigrationExecutor
import java.time.Instant

public class Migrator(
    private val executor: MigrationExecutor,
    private val autoDialect: AutoMigrateDialect,
    private val dialect: CoreDialect
) {
    public suspend fun migrateTo(migration: Migration): Int {
        executor.executeStatement(autoDialect.ensureMigrationTableCreated())
        val appliedMigrations = executor.listAppliedMigrations(autoDialect)
        val requiredMigrations = resolveRequiredMigrations(appliedMigrations, migration)
        for (current in requiredMigrations) {
            val up = current.upStatement(dialect)
            val stmt = listOf(
                autoDialect.transactionStart(),
                up,
                autoDialect.insertMigrationIntoLog(hashMigrationId(current.id), Instant.now()),
                autoDialect.transactionCommit()
            ).joinToString("\n\n")
            executor.executeStatement(stmt)
        }

        return requiredMigrations.size
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
