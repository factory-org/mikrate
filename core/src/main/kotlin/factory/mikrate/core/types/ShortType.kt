package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.TypeSqlGen

public class ShortType : DbType {
    override fun supports(dialect: TypeSqlGen): Boolean = dialect.supportsShort()

    override fun toSql(dialect: TypeSqlGen): String = dialect.short()
}
