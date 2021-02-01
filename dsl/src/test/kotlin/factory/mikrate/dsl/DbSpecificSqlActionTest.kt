package factory.mikrate.dsl

import factory.mikrate.dialects.generic.SqliteCoreDialect
import factory.mikrate.dialects.postgres.PostgresCoreDialect
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.test.AssertionMode
import io.kotest.matchers.string.shouldContain

class DbSpecificSqlActionTest : ShouldSpec({
    assertions = AssertionMode.Warn

    should("generate scripts for all databases") {
        val mig = migration("test") {
            up {
                sql {
                    "postgres" uses "SELECT version();"
                    "sqlite" uses "SELECT sqlite_version();"
                }
            }
        }
        mig.upStatement(PostgresCoreDialect) shouldContain "SELECT version();"
        mig.upStatement(SqliteCoreDialect) shouldContain "SELECT sqlite_version();"
    }
})
