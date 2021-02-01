package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.TypeSqlGen

public object GenericTypeSqlGen : TypeSqlGen {
    override fun boolean(): String = "BOOLEAN"

    override fun supportsBoolean(): Boolean = true

    override fun byte(): String = "BYTE"

    override fun supportsByte(): Boolean = true

    override fun integer(size: Short): String = "INTEGER($size)"

    override fun supportsInteger(size: Short): Boolean = true

    override fun long(): String = "LONG"

    override fun supportsLong(): Boolean = true

    override fun short(): String = "SHORT"

    override fun supportsShort(): Boolean = true

    override fun text(): String = "TEXT"

    override fun supportsText(): Boolean = true

    override fun uuid(): String = "UUID"

    override fun supportsUUID(): Boolean = true

    override fun varchar(length: Int): String = "VARCHAR($length)"

    override fun supportsVarchar(length: Int): Boolean = true
}
