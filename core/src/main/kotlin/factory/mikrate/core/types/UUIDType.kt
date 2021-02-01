package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.TypeSqlGen

public object UUIDType : DbType {
    override fun supports(dialect: TypeSqlGen): Boolean = dialect.supportsUUID()

    override fun toSql(dialect: TypeSqlGen): String = dialect.uuid()
}
