package factory.mikrate.core

import factory.mikrate.dialects.api.TypeSqlGen
import factory.mikrate.dialects.api.models.DialectDbType

public abstract class DbType {
    public abstract val dialectDbType: DialectDbType
    public infix fun supports(dialect: TypeSqlGen): Boolean {
        return dialect.supportsType(dialectDbType)
    }
}
