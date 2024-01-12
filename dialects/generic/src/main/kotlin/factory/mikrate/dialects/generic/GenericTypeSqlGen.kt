package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.TypeSqlGen
import factory.mikrate.dialects.api.models.DialectDbType

public object GenericTypeSqlGen : TypeSqlGen {
    override fun supportsType(dbType: DialectDbType): Boolean = true

    internal fun mapType(dbType: DialectDbType): String = when (dbType) {
        DialectDbType.BooleanType -> "BOOLEAN"
        DialectDbType.ByteType -> "BYTE"
        is DialectDbType.IntegerType -> "INTEGER(size = ${dbType.size})"
        DialectDbType.TextType -> "TEXT"
        is DialectDbType.VarcharType -> "VARCHAR(length = ${dbType.length})"
        DialectDbType.UuidType -> "UUID"
        is DialectDbType.EnumType -> "ENUM(name = `${dbType.name}`)"
        is DialectDbType.JsonType -> "JSON(preserveExact = `${dbType.preserveExact}`)"
        is DialectDbType.Other -> "OTHER(type = `$dbType`)"
    }
}
