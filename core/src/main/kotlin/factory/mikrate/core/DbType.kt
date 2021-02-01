package factory.mikrate.core

import factory.mikrate.dialects.api.TypeSqlGen

public interface DbType {
    public infix fun supports(dialect: TypeSqlGen): Boolean

    public fun toSql(dialect: TypeSqlGen): String
}
