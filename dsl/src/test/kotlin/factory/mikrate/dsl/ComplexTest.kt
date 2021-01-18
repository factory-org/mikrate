package factory.mikrate.dsl

import factory.mikrate.core.Dialect
import factory.mikrate.core.types.VarcharType
import factory.mikrate.dsl.matching.shouldMatchSqlString
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.ShouldSpec

class ComplexTest : ShouldSpec({
    should("correctly work to create tables") {
        val mig = migration("test") {
            up {
                createTable("testTable") {
                    column("test", VarcharType(64)) {
                        nullable()
                        unique()
                    }
                }
            }
        }
        assertSoftly {
            //language=PostgreSQL
            mig.upStatement(Dialect.Postgres) shouldMatchSqlString """
                CREATE TABLE testTable (
                    test varchar(64) NOT NULL constraint uix_testTable_test unique
                );
            """.trimIndent()
            //language=SQLite
            mig.upStatement(Dialect.Sqlite) shouldMatchSqlString """
                CREATE TABLE testTable (
                    test TEXT NOT NULL constraint uix_testTable_test unique
                );
            """.trimIndent()
        }
    }
})
