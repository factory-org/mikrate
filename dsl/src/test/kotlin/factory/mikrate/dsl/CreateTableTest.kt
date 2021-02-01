package factory.mikrate.dsl

import factory.mikrate.core.types.BooleanType
import factory.mikrate.core.types.VarcharType
import factory.mikrate.dialects.generic.GenericCoreDialect
import factory.mikrate.dsl.matching.shouldMatchSqlString
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
        //language=GenericSQL
        mig.upStatement(GenericCoreDialect) shouldMatchSqlString """
            CREATE TABLE testTable (
                test VARCHAR(64) constraint uix_testTable_test unique
            );
        """.trimIndent()
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
        //language=GenericSQL
        mig.upStatement(GenericCoreDialect) shouldMatchSqlString """
            CREATE TABLE testTable (
                testA VARCHAR(64) NOT NULL,
                testB BOOLEAN NOT NULL,
                constraint uix_testTable_testA_testB unique (testA, testB)
            );
        """.trimIndent()
    }
})
