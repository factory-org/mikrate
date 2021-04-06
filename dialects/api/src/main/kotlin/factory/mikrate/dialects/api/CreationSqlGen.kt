package factory.mikrate.dialects.api

public interface CreationSqlGen {
    public fun column(name: String, type: String, nullable: Boolean, unique: String?): String
    public fun table(name: String, ifNotExists: Boolean, columns: List<String>, constraints: List<String>): String
    public fun columnSupported(name: String, nullable: Boolean, unique: String?): SupportStatus
    public fun uniqueSupported(name: String, columns: List<String>): SupportStatus
    public fun tableSupported(name: String, ifNotExists: Boolean): SupportStatus
}
