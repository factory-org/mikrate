package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.TypeSqlGen

public class VarcharType(public val length: Int) : DbType {
    init {
        if (length <= 0) {
            throw IllegalArgumentException("\"length\" cannot be 0 or negative")
        }
    }

    override fun supports(dialect: TypeSqlGen): Boolean = dialect.supportsVarchar(length)

    override fun toSql(dialect: TypeSqlGen): String = dialect.varchar(length)
}
