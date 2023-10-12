package factory.mikrate.dialects.api

public interface AutoMigrateDialect {
    public fun ensureMigrationTableCreated(): String
    public fun transactionPre(): String
    public fun transactionPost(): String
    public fun insertMigrationIntoLog(id: ByteArray, timestamp: String): String
    public fun queryMigrationLog(): String
}
