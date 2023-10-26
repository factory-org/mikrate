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
        DialectDbType.ByteType -> "TINYINT"
        is DialectDbType.IntegerType -> when (dbType.size) {
            2.toShort() -> "SMALLINT"
            4.toShort() -> "INTEGER"
            8.toShort() -> "BIGINT"
            else -> "INT"
        }
        DialectDbType.TextType -> "TEXT"
        is DialectDbType.VarcharType -> "VARCHAR(${dbType.length})"
        DialectDbType.UuidType -> "BLOB"
        is DialectDbType.EnumType,
        is DialectDbType.Other -> throw IllegalArgumentException("Other type is not supported")
    }

    private val supportedIntegerSizes = setOf<Short>(1, 2, 3, 4, 6, 8)
}
