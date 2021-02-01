package factory.mikrate.dsl.builders

import factory.mikrate.dsl.MigrationDsl
import org.intellij.lang.annotations.Language

@MigrationDsl
public class DbSpecificSqlBuilder {
    private val mapping: MutableMap<String, String?> = mutableMapOf()

    public fun skip(dialect: String) {
        mapping[dialect] = null
    }

    public infix fun String.uses(@Language("GenericSQL") sql: String) {
        mapping[this] = sql
    }

    public fun build(): Map<String, String?> {
        return mapping
    }
}
