package factory.mikrate.dialects.h2

import factory.mikrate.dialects.api.NotAvailableError
import factory.mikrate.dialects.api.TypeSqlGen
import factory.mikrate.dialects.api.models.DialectDbType

public class H2TypeSqlGen(public val quoteIdentifiers: Boolean) : TypeSqlGen {
    override fun supportsType(dbType: DialectDbType): Boolean = when (dbType) {
        DialectDbType.BooleanType,
        DialectDbType.TextType,
        DialectDbType.UuidType,
        is DialectDbType.VarcharType,
        DialectDbType.ByteType,
        is DialectDbType.EnumType -> true
        is DialectDbType.IntegerType -> supportedIntegerSizes.contains(dbType.size)
        is DialectDbType.Other -> false
    }

    internal fun mapType(dbType: DialectDbType): String = when (dbType) {
        DialectDbType.BooleanType -> "BOOLEAN"
        is DialectDbType.IntegerType -> when (dbType.size) {
            1.toShort() -> "TINYINT"
            2.toShort() -> "SMALLINT"
            4.toShort() -> "INTEGER"
            8.toShort() -> "BIGINT"
            else -> throw NotAvailableError("H2 doesn't support integer sizes other than 1, 2, 4 or 8")
        }
        DialectDbType.TextType -> "CHARACTER LARGE OBJECT"
        is DialectDbType.VarcharType -> "VARCHAR2(${dbType.length})"
        DialectDbType.UuidType -> "UUID"
        DialectDbType.ByteType -> "TINYINT"
        is DialectDbType.EnumType -> if (quoteIdentifiers) "\"${dbType.name}\"" else dbType.name
        is DialectDbType.Other -> throw NotAvailableError("H2 doesn't support this type")
    }

    private val supportedIntegerSizes = setOf<Short>(1, 2, 4, 8)
}
