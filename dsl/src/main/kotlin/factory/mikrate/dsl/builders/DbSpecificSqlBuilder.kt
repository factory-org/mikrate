package factory.mikrate.dsl.builders

import factory.mikrate.core.Dialect
import factory.mikrate.dsl.MigrationDsl
import org.intellij.lang.annotations.Language

@MigrationDsl
public class DbSpecificSqlBuilder {
    private val mapping: MutableMap<Dialect, String?> = mutableMapOf()

    public fun postgres(@Language("PostgreSQL") script: String) {
        mapping[Dialect.Postgres] = script
    }

    public fun skipPostgres() {
        mapping[Dialect.Postgres] = null
    }

    public fun sqlite(@Language("SQLite") script: String) {
        mapping[Dialect.Sqlite] = script
    }

    public fun skipSqlite() {
        mapping[Dialect.Sqlite] = null
    }

    public fun build(): Map<Dialect, String?> {
        return mapping
    }
}
