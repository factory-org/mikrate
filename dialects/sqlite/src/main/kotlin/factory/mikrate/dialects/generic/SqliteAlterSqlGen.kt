package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.AlterSqlGen
import factory.mikrate.dialects.api.SupportStatus

public object SqliteAlterSqlGen : AlterSqlGen {
    override fun renameColumn(table: String, column: String, to: String): String {
        //language=SQLite
        return "ALTER TABLE $table RENAME COLUMN $column TO $to;"
    }

    override fun renameColumnSupported(table: String, column: String, newName: String): SupportStatus =
        SupportStatus.Supported
}
