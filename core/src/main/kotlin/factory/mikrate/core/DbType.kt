package factory.mikrate.core

public interface DbType {
    public infix fun supports(dialect: Dialect): Boolean

    public fun toSql(dialect: Dialect): String
}
