package factory.mikrate.dialects.sqlite

import factory.mikrate.dialects.api.TypeSqlGen
import factory.mikrate.dialects.api.models.DialectDbType

public object SqliteTypeSqlGen : TypeSqlGen {
    override fun supportsType(dbType: DialectDbType): Boolean = when (dbType) {
        is DialectDbType.IntegerType -> supportedIntegerSizes.contains(dbType.size)
        DialectDbType.BooleanType,
        DialectDbType.ByteType,
        DialectDbType.TextType,
        DialectDbType.UuidType,
        is DialectDbType.VarcharType -> true
        is DialectDbType.EnumType,
        is DialectDbType.Other -> false
    }

    internal fun mapType(dbType: DialectDbType): String = when (dbType) {
        DialectDbType.BooleanType -> "NUMERIC"
        DialectDbType.ByteType,
        is DialectDbType.IntegerType -> "INTEGER"
        DialectDbType.TextType,
        is DialectDbType.VarcharType -> "Text"
        DialectDbType.UuidType -> "BLOB"
        is DialectDbType.EnumType,
        is DialectDbType.Other -> throw IllegalArgumentException("Other type is not supported")
    }

    private val supportedIntegerSizes = setOf<Short>(1, 2, 3, 4, 6, 8)
}
