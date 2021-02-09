package factory.mikrate.dialects.postgres

import factory.mikrate.core.types.IntegerType
import factory.mikrate.dsl.migration
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.testcontainers.perTest
import io.kotest.matchers.ints.shouldBeExactly

class CreateTableTest : ShouldSpec({
    val postgresContainer = createPostgresContainer()
    listener(postgresContainer.perTest())

    should("create a basic table") {
        val conn = postgresContainer.connect()
        val createTable by migration {
            up {
                createTable("TestTable") {
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
            it.executeUpdate("""INSERT INTO TestTable VALUES (1);""") shouldBeExactly 1
        }
    }
})
