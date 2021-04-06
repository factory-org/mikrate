package factory.mikrate.dialects.postgres

import factory.mikrate.dialects.api.NotAvailableError
import factory.mikrate.dialects.api.TypeNotAvailableError
import factory.mikrate.dialects.api.TypeSqlGen

public class PostgresTypeSqlGen(protected val version: PostgresVersion) : TypeSqlGen {
    override fun boolean(): String = "boolean"

    override fun supportsBoolean(): Boolean = true

    override fun byte(): String = throw TypeNotAvailableError("Byte is currently not supported in postgres")

    override fun supportsByte(): Boolean = false

    override fun integer(size: Short): String = when (size) {
        2.toShort() -> "smallint"
        4.toShort() -> "integer"
        8.toShort() -> "bigint"
        else -> throw NotAvailableError("Postgres doesn't support integer sizes other than 2, 4 or 8")
    }

    override fun supportsInteger(size: Short): Boolean = listOf<Short>(2, 4, 8).contains(size)

    override fun text(): String = "text"

    override fun supportsText(): Boolean = true

    override fun uuid(): String = "uuid"

    override fun supportsUUID(): Boolean = true

    override fun varchar(length: Int): String = "varchar($length)"

    override fun supportsVarchar(length: Int): Boolean = true
}
