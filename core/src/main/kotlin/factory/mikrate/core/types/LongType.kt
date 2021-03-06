package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.TypeSqlGen

public class LongType : DbType {
    override fun supports(dialect: TypeSqlGen): Boolean = dialect.supportsLong()

    override fun toSql(dialect: TypeSqlGen): String = dialect.long()
}
