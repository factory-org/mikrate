package factory.mikrate.dsl.builders

import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.dsl.MigrationDsl
import org.intellij.lang.annotations.Language

/**
 * Executes different SQL string defending on the dialect.
 *
 * @see ActionBuilder.sql
 */
@MigrationDsl
public class DbSpecificSqlBuilder {
    private val mapping: MutableMap<String, String?> = mutableMapOf()

    /**
     * Configures a dialect to not execute any SQL.
     */
    public fun skip(dialect: String) {
        mapping[dialect] = null
    }

    /**
     * Configures a dialect to not execute any SQL.
     */
    public fun skip(dialect: CoreDialect) {
        mapping[dialect.id] = null
    }

    /**
     * Defines an SQL string to execute for the given dialect.
     *
     * ```kotlin
     * "sqlite" uses "SELECT a ..."
     * "postgres" uses "SELECT b ..."
     * ```
     *
     * @receiver The dialect id
     * @param sql The SQL string to be executed for that dialect
     */
    public infix fun String.uses(@Language("SQL") sql: String) {
        mapping[this] = sql
    }

    /**
     * Defines an SQL string to execute for the given dialect.
     *
     * ```kotlin
     * SqliteCoreDialect uses "SELECT a ..."
     * PostgresCoreDialect uses "SELECT b ..."
     * ```
     *
     * @receiver The dialect
     * @param sql The SQL string to be executed for that dialect
     */
    public infix fun CoreDialect.uses(@Language("SQL") sql: String) {
        mapping[this.id] = sql
    }

    /**
     * @suppress
     */
    public fun build(): Map<String, String?> {
        return mapping
    }
}
