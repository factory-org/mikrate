package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.core.Dialect

public class VarcharType(public val length: Int) : DbType {
    override fun supports(dialect: Dialect): Boolean = true

    override fun toSql(dialect: Dialect): String {
        return when (dialect) {
            Dialect.Sqlite -> "TEXT"
            Dialect.Postgres -> "varchar($length)"
        }
    }
}
