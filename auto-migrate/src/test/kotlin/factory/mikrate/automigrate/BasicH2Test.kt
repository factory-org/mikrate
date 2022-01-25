package factory.mikrate.automigrate

import factory.mikrate.core.types.IntegerType
import factory.mikrate.dialects.h2.H2CoreDialect
import factory.mikrate.dialects.h2.auto.H2AutoDialect
import factory.mikrate.dsl.migration
import factory.mikrate.executors.r2dbc.R2dbcExecutor
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactories
import kotlinx.coroutines.reactive.awaitSingle

@Suppress("unused")
class BasicH2Test : ShouldSpec({
    should("create small h2 table") {
        val conn: Connection = ConnectionFactories.get("r2dbc:h2:mem:///testdb").create().awaitSingle()
        val exec = R2dbcExecutor(conn)
        val mig = Migrator(exec, H2AutoDialect, H2CoreDialect)

        val simple by migration {
            up {
                createTable("TestTable") {
                    column("test", IntegerType)
                }
            }
        }

        mig.migrateTo(simple) shouldBeExactly 1

        val result = conn.createStatement("INSERT INTO TestTable VALUES (1)").execute().awaitSingle()
        result.rowsUpdated.awaitSingle() shouldBeExactly 1
    }
})
