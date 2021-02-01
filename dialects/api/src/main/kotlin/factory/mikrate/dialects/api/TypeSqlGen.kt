package factory.mikrate.dialects.api

public interface TypeSqlGen {
    public fun boolean(): String
    public fun supportsBoolean(): Boolean

    public fun byte(): String
    public fun supportsByte(): Boolean

    public fun integer(size: Short): String
    public fun supportsInteger(size: Short): Boolean

    public fun long(): String = integer(8)
    public fun supportsLong(): Boolean = supportsInteger(8)

    public fun short(): String = integer(2)
    public fun supportsShort(): Boolean = supportsInteger(2)

    public fun text(): String
    public fun supportsText(): Boolean

    public fun uuid(): String
    public fun supportsUUID(): Boolean

    public fun varchar(length: Int): String
    public fun supportsVarchar(length: Int): Boolean
}
