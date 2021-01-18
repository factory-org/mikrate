package factory.mikrate.dsl

import factory.mikrate.core.Dialect
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.test.AssertionMode
import io.kotest.matchers.string.shouldContain

class DbSpecificSqlActionTest : ShouldSpec({
    assertions = AssertionMode.Warn

    should("generate scripts for all databases") {
        val mig = migration("test") {
            up {
                sql {
                    postgres("SELECT version();")
                    sqlite("SELECT sqlite_version();")
                }
            }
        }
        mig.upStatement(Dialect.Postgres) shouldContain "SELECT version();"
        mig.upStatement(Dialect.Sqlite) shouldContain "SELECT sqlite_version();"
    }
})
