package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.TypeSqlGen

public object BooleanType : DbType {
    override fun supports(dialect: TypeSqlGen): Boolean = dialect.supportsBoolean()

    override fun toSql(dialect: TypeSqlGen): String {
        return dialect.boolean()
    }
}
