package factory.mikrate.dialects.api

import factory.mikrate.dialects.api.models.DialectDbType

public interface TypeSqlGen {
    public fun supportsType(dbType: DialectDbType): Boolean
}
