package factory.mikrate.dialects.postgres

import factory.mikrate.dialects.api.NotAvailableError
import factory.mikrate.dialects.api.TypeSqlGen
import factory.mikrate.dialects.api.models.DialectDbType

public class PostgresTypeSqlGen(protected val version: PostgresVersion) : TypeSqlGen {
    override fun supportsType(dbType: DialectDbType): Boolean = when (dbType) {
        DialectDbType.BooleanType,
        DialectDbType.TextType,
        DialectDbType.UuidType,
        is DialectDbType.VarcharType -> true
        is DialectDbType.IntegerType -> supportedIntegerSizes.contains(dbType.size)
        DialectDbType.ByteType,
        is DialectDbType.EnumType,
        is DialectDbType.JsonType -> true
        is DialectDbType.Other -> false
    }

    internal fun mapType(dbType: DialectDbType): String = when (dbType) {
        DialectDbType.BooleanType -> "boolean"
        is DialectDbType.IntegerType -> when (dbType.size) {
            2.toShort() -> "smallint"
            4.toShort() -> "integer"
            8.toShort() -> "bigint"
            else -> throw NotAvailableError("Postgres doesn't support integer sizes other than 2, 4 or 8")
        }
        DialectDbType.TextType -> "text"
        is DialectDbType.VarcharType -> "varchar(${dbType.length})"
        DialectDbType.UuidType -> "uuid"
        DialectDbType.ByteType,
        is DialectDbType.EnumType -> (dbType as DialectDbType.EnumType).name
        is DialectDbType.JsonType -> if (dbType.preserveExact) "json" else "jsonb"
        is DialectDbType.Other -> throw NotAvailableError("Postgres doesn't support this type")
    }

    private val supportedIntegerSizes = setOf<Short>(2, 4, 8)
}
