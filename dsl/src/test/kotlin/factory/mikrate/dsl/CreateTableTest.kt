package factory.mikrate.dsl

import factory.mikrate.core.types.BooleanType
import factory.mikrate.core.types.VarcharType
import factory.mikrate.dialects.generic.GenericCoreDialect
import factory.mikrate.dsl.helpers.ColumnRef
import factory.mikrate.dsl.helpers.TableRef
import factory.mikrate.dsl.matching.shouldMatchSqlString
import io.kotest.core.spec.style.ShouldSpec

class CreateTableTest : ShouldSpec({
    should("correctly work to create tables") {
        val foreignColumn = ColumnRef("ForeignTable", "foreignColumn")
        val mig = migration("test") {
            up {
                createTable("TestTable") {
                    column("testA", VarcharType(64)) {
                        nullable()
                        unique()
                    }
                    column("testB", VarcharType(64)) {
                        referencesForeign(foreignColumn)
                    }
                }
            }
        }
        //language=GenericSQL
        mig.upStatement(GenericCoreDialect) shouldMatchSqlString """
            CREATE TABLE TestTable (
                testA VARCHAR(length = 64) CONSTRAINT uix_TestTable_testA UNIQUE,
                testB VARCHAR(length = 64) NOT NULL CONSTRAINT fk_TestTable_testB_ForeignTable_foreignColumn FOREIGN KEY REFERENCES ForeignTable(ForeignTable)
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
                testA VARCHAR(length = 64) NOT NULL,
                testB BOOLEAN NOT NULL,
                CONSTRAINT uix_testTable_testA_testB UNIQUE (testA, testB)
            );
        """.trimIndent()
    }
    should("create a foreign keys") {
        val foreignTable = TableRef("ForeignTable")
        val mig = migration("test") {
            up {
                createTable("testTable") {
                    val a = column("testA", VarcharType(64))
                    val b = column("testB", BooleanType)
                    foreignKeys(foreignTable, a.column to "foreignColumn1", b.column to "foreignColumn1")
                }
            }
        }
        //language=GenericSQL
        mig.upStatement(GenericCoreDialect) shouldMatchSqlString """
            CREATE TABLE testTable (
                testA VARCHAR(length = 64) NOT NULL,
                testB BOOLEAN NOT NULL,
                CONSTRAINT uix_testTable_testA_testB_ForeignTable_foreignColumn1_foreignColumn1 FOREIGN KEY (testA, testB) REFERENCES (foreignColumn1, foreignColumn1)
            );
        """.trimIndent()
    }
})
