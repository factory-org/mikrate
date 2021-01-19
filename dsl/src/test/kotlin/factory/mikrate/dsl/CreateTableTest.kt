package factory.mikrate.dsl

import factory.mikrate.core.Dialect
import factory.mikrate.core.types.BooleanType
import factory.mikrate.core.types.VarcharType
import factory.mikrate.dsl.matching.shouldMatchSqlString
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.ShouldSpec

class CreateTableTest : ShouldSpec({
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
                    test varchar(64) constraint uix_testTable_test unique
                );
            """.trimIndent()
            //language=SQLite
            mig.upStatement(Dialect.Sqlite) shouldMatchSqlString """
                CREATE TABLE testTable (
                    test TEXT constraint uix_testTable_test unique
                );
            """.trimIndent()
        }
    }
    should("create a unique constraint") {
        val mig = migration("test") {
            up {
                createTable("testTable") {
                    val a = column("testA", VarcharType(64))
                    val b = column("testB", BooleanType)
                    unique(a, b)
                }
            }
        }
        assertSoftly {
            //language=PostgreSQL
            mig.upStatement(Dialect.Postgres) shouldMatchSqlString """
                CREATE TABLE testTable (
                    testA varchar(64) NOT NULL,
                    testB boolean NOT NULL,
                    constraint uix_testTable_testA_testB unique (testA, testB)
                );
            """.trimIndent()
            //language=SQLite
            mig.upStatement(Dialect.Sqlite) shouldMatchSqlString """
                CREATE TABLE testTable (
                    testA TEXT NOT NULL,
                    testB NUMERIC NOT NULL,
                    constraint uix_testTable_testA_testB unique (testA, testB)
                );
            """.trimIndent()
        }
    }
})
