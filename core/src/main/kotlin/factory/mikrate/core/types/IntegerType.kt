package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.TypeSqlGen

public open class IntegerType(public val size: Short = 4) : DbType {
    init {
        if (size <= 0) {
            throw IllegalArgumentException("\"size\" cannot be 0 or negative")
        }
    }

    override fun supports(dialect: TypeSqlGen): Boolean {
        return dialect.supportsInteger(size)
    }

    override fun toSql(dialect: TypeSqlGen): String {
        return dialect.integer(size)
    }

    public companion object : IntegerType()

    override fun toString(): String = "IntegerType(size = $size)"
}
