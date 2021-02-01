package factory.mikrate.dsl

import factory.mikrate.dialects.generic.GenericCoreDialect
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.test.AssertionMode
import io.kotest.matchers.string.shouldContain

@Suppress("SqlNoDataSourceInspection")
class SqlActionTest : ShouldSpec({
    assertions = AssertionMode.Warn

    should("be callable with script") {
        val mig = migration("test") {
            up {
                sql("SELECT * FROM abc")
            }
        }
        mig.upStatement(GenericCoreDialect) shouldContain "SELECT * FROM abc"
    }
})
