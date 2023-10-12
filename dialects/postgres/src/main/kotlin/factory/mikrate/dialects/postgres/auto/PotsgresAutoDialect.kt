package factory.mikrate.dialects.postgres.auto

import factory.mikrate.dialects.api.AutoMigrateDialect

public object PotsgresAutoDialect : AutoMigrateDialect {
    override fun ensureMigrationTableCreated(): String {
        //language=PostgreSQL
        return """
            CREATE TABLE IF NOT EXISTS AutoMigrations (
                id blob primary key,
                timestamp text
            )
        """.trimIndent()
    }

    override fun transactionPre(): String {
        //language=PostgreSQL
        return """
            BEGIN TRANSACTION;
        """.trimIndent()
    }

    override fun transactionPost(): String {
        //language=PostgreSQL
        return "COMMIT;"
    }

    override fun insertMigrationIntoLog(id: ByteArray, timestamp: String): String {
        //language=PostgreSQL
        return """
            INSERT INTO AutoMigrations VALUES (X'${id.hex()}', '$timestamp');
        """.trimIndent()
    }

    override fun queryMigrationLog(): String {
        //language=PostgreSQL
        return """
            SELECT id, timestamp FROM AutoMigrations;
        """.trimIndent()
    }

    private fun ByteArray.hex(): String = joinToString("") { "%02x".format(it) }
}
