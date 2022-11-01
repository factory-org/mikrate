package factory.mikrate.dialects.h2

import factory.mikrate.dialects.api.AlterSqlGen
import factory.mikrate.dialects.api.SupportStatus

public open class H2AlterSqlGen(private val quoteIdentifiers: Boolean) : AlterSqlGen {
    override fun renameColumn(table: String, column: String, to: String): String {
        val quotedTable = if (quoteIdentifiers) "\"$table\"" else table
        val quotedColumn = if (quoteIdentifiers) "\"$column\"" else column
        val quotedTo = if (quoteIdentifiers) "\"$to\"" else to
        //language=H2
        return "ALTER TABLE $quotedTable RENAME COLUMN $quotedColumn TO $quotedTo;"
    }

    override fun renameColumnSupported(table: String, column: String, newName: String): SupportStatus =
        SupportStatus.Supported
}
