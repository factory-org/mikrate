package factory.mikrate.dialects.sqlite.auto

import factory.mikrate.dialects.api.AutoMigrateDialect

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

    override fun transactionPre(): String {
        //language=SQLite
        return ""
    }

    override fun transactionPost(): String {
        //language=SQLite
        return ""
    }

    override fun insertMigrationIntoLog(id: ByteArray, timestamp: String): String {
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
