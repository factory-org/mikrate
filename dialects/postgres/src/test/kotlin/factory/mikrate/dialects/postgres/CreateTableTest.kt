package factory.mikrate.dialects.postgres

import factory.mikrate.core.types.IntegerType
import factory.mikrate.core.types.ShortType
import factory.mikrate.core.types.UuidType
import factory.mikrate.core.types.VarcharType
import factory.mikrate.dsl.migration
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.testcontainers.perTest
import io.kotest.matchers.ints.shouldBeExactly
import java.sql.SQLException

class CreateTableTest : ShouldSpec({
    val postgresContainer = createPostgresContainer()
    listener(postgresContainer.perTest())

    should("create a basic table") {
        val conn = postgresContainer.connect()
        val createTable by migration {
            up {
                createTable("TestTable1") {
                    column("id", IntegerType) {
                        primary()
                    }
                }
            }
        }

        // Run against DB
        conn.createStatement().use {
            it.executeUpdate(createTable.upStatement(PostgresCoreDialect))
        }

        // Verify creation
        conn.createStatement().use {
            it.executeUpdate("""INSERT INTO TestTable1 VALUES (1);""") shouldBeExactly 1
        }
    }

    should("create a basic table with unique constraints") {
        val conn = postgresContainer.connect()
        val createTable by migration {
            up {
                createTable("TestTable1") {
                    column("id", IntegerType) {
                        primary()
                    }
                    val secondary = column("secondary", UuidType)
                    val tertiary = column("tertiary", VarcharType(10))
                    unique(secondary, tertiary)
                    column("uni", ShortType) {
                        unique()
                    }
                }
            }
        }

        // Run against DB
        conn.createStatement().use {
            it.executeUpdate(createTable.upStatement(PostgresCoreDialect))
        }

        // Insert one value
        conn.createStatement().use {
            it.executeUpdate("""INSERT INTO TestTable1 VALUES (1, '2d2e272e-6051-48e7-9eaf-efa3e377ca13', '1234567890', 2);""") shouldBeExactly 1
        }
        // Insert another value
        conn.createStatement().use {
            it.executeUpdate("""INSERT INTO TestTable1 VALUES (2, '2d2e272e-6051-48e7-9eaf-efa3e377ca13', '0987654321', 3);""") shouldBeExactly 1
        }
        // Insert the same value again
        shouldThrow<SQLException> {
            conn.createStatement().use {
                it.executeUpdate("""INSERT INTO TestTable1 VALUES (3, '2d2e272e-6051-48e7-9eaf-efa3e377ca13', '0987654321', 4);""")
            }
        }
        shouldThrow<SQLException> {
            conn.createStatement().use {
                it.executeUpdate("""INSERT INTO TestTable1 VALUES (3, '2d2e272e-6051-48e7-9eaf-efa3e377ca13', '2345678901', 3);""")
            }
        }
    }

    should("create a basic tables with foreign keys") {
        val conn = postgresContainer.connect()
        val createTable by migration {
            up {
                val table1 = createTable("TestTable1") {
                    column("id", IntegerType) {
                        primary()
                        unique()
                    }
                    val secondary = column("secondary", ShortType)
                    val tertiary = column("tertiary", VarcharType(10))
                    unique(secondary, tertiary)
                }

                createTable("TestTable2") {
                    column("id", IntegerType) {
                        unique()
                        referencesForeign(table1.column("id"))
                    }
                    val secondary = column("secondary", ShortType)
                    val tertiary = column("tertiary", VarcharType(10))
                    foreignKeys(
                        table1,
                        secondary to table1.column("secondary"),
                        tertiary to table1.column("tertiary")
                    )
                }
            }
        }

        // Run against DB
        conn.createStatement().use {
            val stmt = createTable.upStatement(PostgresCoreDialect)
            it.executeUpdate(stmt)
        }

        // Insert one value
        conn.createStatement().use {
            it.executeUpdate("""INSERT INTO TestTable1 VALUES (1, 1, '1234567890');""") shouldBeExactly 1
            it.executeUpdate("""INSERT INTO TestTable1 VALUES (2, 1, '2345678901');""") shouldBeExactly 1
            it.executeUpdate("""INSERT INTO TestTable1 VALUES (3, 2, '3456789012');""") shouldBeExactly 1
            it.executeUpdate("""INSERT INTO TestTable1 VALUES (4, 3, '3456789012');""") shouldBeExactly 1
        }
        // Insert another value
        conn.createStatement().use {
            it.executeUpdate("""INSERT INTO TestTable2 VALUES (1, 1, '1234567890');""") shouldBeExactly 1
            it.executeUpdate("""INSERT INTO TestTable2 VALUES (2, 1, '1234567890');""") shouldBeExactly 1
        }
        // Insert something not in foreign table
        shouldThrow<SQLException> {
            conn.createStatement().use {
                it.executeUpdate("""INSERT INTO TestTable2 VALUES (3, 2, '2345678901');""")
            }
        }
        shouldThrow<SQLException> {
            conn.createStatement().use {
                it.executeUpdate("""INSERT INTO TestTable2 VALUES (5, 3, '2345678901');""")
            }
        }
    }
})
