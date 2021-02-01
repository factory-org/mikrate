package factory.mikrate.dialects.api

public interface AutoMigrateDialect : CoreDialect {
    public val autoMigrate: AutoMigrateSqlGen
}
