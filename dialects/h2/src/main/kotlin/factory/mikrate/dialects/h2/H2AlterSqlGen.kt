package factory.mikrate.dialects.h2

import factory.mikrate.dialects.api.AlterSqlGen
import factory.mikrate.dialects.api.SupportStatus

public open class H2AlterSqlGen : AlterSqlGen {
    override fun renameColumn(table: String, column: String, to: String): String {
        //language=H2
        return "ALTER TABLE $table RENAME COLUMN $column TO $to;"
    }

    override fun renameColumnSupported(table: String, column: String, newName: String): SupportStatus =
        SupportStatus.Supported
}
