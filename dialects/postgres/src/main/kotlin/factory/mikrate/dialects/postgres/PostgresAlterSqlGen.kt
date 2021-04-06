package factory.mikrate.dialects.postgres

import factory.mikrate.dialects.api.AlterSqlGen
import factory.mikrate.dialects.api.SupportStatus

public open class PostgresAlterSqlGen(protected val version: PostgresVersion) : AlterSqlGen {
    override fun renameColumn(table: String, column: String, to: String): String {
        //language=PostgreSQL
        return "ALTER TABLE $table RENAME COLUMN $column TO $to;"
    }

    override fun renameColumnSupported(table: String, column: String, newName: String): SupportStatus =
        SupportStatus.Supported
}
