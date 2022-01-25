package factory.mikrate.dialects.h2.auto

import factory.mikrate.dialects.api.AutoMigrateDialect
import java.time.Instant

public object H2AutoDialect : AutoMigrateDialect {
    override fun ensureMigrationTableCreated(): String {
        //language=H2
        return """
            CREATE TABLE IF NOT EXISTS AutoMigrations (
                id blob primary key,
                timestamp text
            )
        """.trimIndent()
    }

    override fun transactionPre(): String {
        return ""
    }

    override fun transactionPost(): String {
        return ""
    }

    override fun insertMigrationIntoLog(id: ByteArray, timestamp: Instant): String {
        //language=H2
        return """
            INSERT INTO AutoMigrations VALUES (X'${id.hex()}', '$timestamp');
        """.trimIndent()
    }

    override fun queryMigrationLog(): String {
        //language=H2
        return """
            SELECT id, timestamp FROM AutoMigrations;
        """.trimIndent()
    }

    private fun ByteArray.hex(): String = joinToString("") { "%02x".format(it) }
}
