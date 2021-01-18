package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.core.Dialect
import factory.mikrate.core.NotAvailableError

public open class IntegerType(public val size: Short = 4) : DbType {
    init {
        if (size <= 0) {
            throw IllegalArgumentException("\"size\" cannot be 0 or negative")
        }
    }

    override fun supports(dialect: Dialect): Boolean {
        return when (dialect) {
            Dialect.Postgres -> {
                when (size) {
                    2.toShort(),
                    4.toShort(),
                    8.toShort() -> true
                    else -> false
                }
            }
            Dialect.Sqlite -> {
                when (size) {
                    1.toShort(),
                    2.toShort(),
                    3.toShort(),
                    4.toShort(),
                    6.toShort(),
                    8.toShort() -> true
                    else -> false
                }
            }
        }
    }

    override fun toSql(dialect: Dialect): String {
        return when (dialect) {
            Dialect.Postgres -> {
                when (size) {
                    2.toShort() -> "smallint"
                    4.toShort() -> "integer"
                    8.toShort() -> "bigint"
                    else -> throw NotAvailableError("Postgres doesn't support integer sizes other than 2, 4 or 8")
                }
            }
            Dialect.Sqlite -> {
                when (size) {
                    1.toShort(),
                    2.toShort(),
                    3.toShort(),
                    4.toShort(),
                    6.toShort(),
                    8.toShort() -> "INTEGER"
                    else -> throw NotAvailableError("SQLite doesn't support integer sizes other than 1, 2, 3, 4, 6 or 8")
                }
            }
        }
    }

    public companion object : IntegerType()

    override fun toString(): String = "IntegerType(size = $size)"
}
