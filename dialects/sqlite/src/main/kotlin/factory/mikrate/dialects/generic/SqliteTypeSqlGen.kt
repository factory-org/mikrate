package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.TypeSqlGen

public object SqliteTypeSqlGen : TypeSqlGen {
    override fun boolean(): String = "NUMERIC"

    override fun supportsBoolean(): Boolean = true

    override fun byte(): String = "INTEGER"

    override fun supportsByte(): Boolean = true

    override fun integer(size: Short): String = "INTEGER"

    override fun supportsInteger(size: Short): Boolean = listOf<Short>(1, 2, 3, 4, 6, 8).contains(size)

    override fun text(): String = "TEXT"

    override fun supportsText(): Boolean = true

    override fun uuid(): String = "BLOB"

    override fun supportsUUID(): Boolean = true

    override fun varchar(length: Int): String = "TEXT"

    override fun supportsVarchar(length: Int): Boolean = true

}
