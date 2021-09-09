package factory.mikrate.automigrate

import factory.mikrate.core.types.IntegerType
import factory.mikrate.dialects.sqlite.SqliteCoreDialect
import factory.mikrate.dialects.sqlite.auto.SqliteAutoDialect
import factory.mikrate.dsl.migration
import factory.mikrate.executors.jdbc.JDBCExecutor
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.ints.shouldBeExactly
import java.sql.DriverManager

class BasicSqliteTest : ShouldSpec({
    should("work for a simple migration in sqlite") {
        val conn = DriverManager.getConnection("jdbc:sqlite::memory:")
        conn.use {
            val exec = JDBCExecutor(conn)
            val mig = Migrator(exec, SqliteAutoDialect, SqliteCoreDialect)

            val simple by migration {
                up {
                    sql("SELECT sqlite_version()")
                }
            }

            mig.migrateTo(simple) shouldBeExactly 1
        }
    }

    should("create small sqlite table") {
        val conn = DriverManager.getConnection("jdbc:sqlite::memory:")
        conn.use {
            val exec = JDBCExecutor(conn)
            val mig = Migrator(exec, SqliteAutoDialect, SqliteCoreDialect)

            val simple by migration {
                up {
                    createTable("TestTable") {
                        column("test", IntegerType)
                    }
                }
            }

            mig.migrateTo(simple) shouldBeExactly 1

            conn.createStatement().use {
                it.executeUpdate("INSERT INTO TestTable VALUES (1)") shouldBeExactly 1
            }
        }
    }
})
