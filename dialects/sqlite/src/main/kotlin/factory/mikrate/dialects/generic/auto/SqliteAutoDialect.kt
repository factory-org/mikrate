package factory.mikrate.dialects.generic.auto

import factory.mikrate.dialects.api.AutoMigrateDialect
import java.time.Instant

public object SqliteAutoDialect : AutoMigrateDialect {
    override fun ensureMigrationTableCreated(): String {
        //language=SQLite
        return """
            CREATE TABLE IF NOT EXISTS AutoMigrations (
                id blob primary key,
                timestamp text
            )
        """.trimIndent()
    }

    override fun transactionStart(): String {
        //language=SQLite
        return """
            BEGIN EXCLUSIVE TRANSACTION;
        """.trimIndent()
    }

    override fun transactionCommit(): String {
        //language=SQLite
        return "COMMIT;"
    }

    override fun insertMigrationIntoLog(id: ByteArray, timestamp: Instant): String {
        //language=SQLite
        return """
            INSERT INTO AutoMigrations VALUES (X'${id.hex()}', '$timestamp');
        """.trimIndent()
    }

    override fun queryMigrationLog(): String {
        return """
            SELECT id, timestamp FROM AutoMigrations;
        """.trimIndent()
    }

    private fun ByteArray.hex(): String = joinToString("") { "%02x".format(it) }
}
