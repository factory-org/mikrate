package factory.mikrate.dialects.api

import java.time.Instant

public interface AutoMigrateDialect {
    public fun ensureMigrationTableCreated(): String
    public fun transactionStart(): String
    public fun transactionCommit(): String
    public fun insertMigrationIntoLog(id: ByteArray, timestamp: Instant): String
    public fun queryMigrationLog(): String

}
