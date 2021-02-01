package factory.mikrate.dialects.api

public interface AutoMigrateSqlGen {
    public fun ensureMigrationTableCreated(): String
}
