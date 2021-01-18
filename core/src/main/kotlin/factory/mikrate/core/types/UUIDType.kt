package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.core.Dialect

public object UUIDType : DbType {
    override fun supports(dialect: Dialect): Boolean = true

    override fun toSql(dialect: Dialect): String {
        return when (dialect) {
            Dialect.Postgres -> "uuid"
            Dialect.Sqlite -> "BLOB"
        }
    }
}
