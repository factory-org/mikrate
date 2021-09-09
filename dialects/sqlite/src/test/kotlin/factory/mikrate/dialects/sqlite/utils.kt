package factory.mikrate.dialects.sqlite

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import org.intellij.lang.annotations.Language

internal inline fun withTestDb(crossinline block: TestDbContext.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    val conn = DriverManager.getConnection("jdbc:sqlite::memory:")
    conn.use {
        val context = TestDbContext(it)
        block(context)
    }
}

class TestDbContext(private val connection: Connection) {
    @Throws(SQLException::class)
    fun update(
        @Language("SQLite")
        statement: String
    ): Int {
        return connection.createStatement().use {
            it.executeUpdate(statement)
        }
    }
}
