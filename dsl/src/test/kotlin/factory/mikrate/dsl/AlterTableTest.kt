package factory.mikrate.dsl

import factory.mikrate.dialects.generic.GenericCoreDialect
import factory.mikrate.dsl.matching.shouldMatchSqlString
import io.kotest.core.spec.style.ShouldSpec

class AlterTableTest : ShouldSpec({
    should("rename the columns") {
        val mig = migration("test") {
            up {
                table("A").column("b").renameTo("c")
                table("B") {
                    renameColumn("f", "g")
                    column("d").renameTo("e")
                }
            }
        }
        //language=GenericSQL
        mig.upStatement(GenericCoreDialect) shouldMatchSqlString """
            ALTER TABLE A RENAME COLUMN b TO c;
            ALTER TABLE B RENAME COLUMN f TO g;
            ALTER TABLE B RENAME COLUMN d TO e;
        """.trimIndent()
    }
})
