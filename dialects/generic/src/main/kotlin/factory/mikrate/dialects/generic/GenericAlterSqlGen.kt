package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.AlterSqlGen
import factory.mikrate.dialects.api.SupportStatus

public object GenericAlterSqlGen : AlterSqlGen {
    override fun renameColumn(table: String, column: String, to: String): String =
        "ALTER TABLE $table RENAME COLUMN $column TO $to;"

    override fun renameColumnSupported(table: String, column: String, newName: String): SupportStatus =
        SupportStatus.Supported
}
