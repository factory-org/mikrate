package factory.mikrate.dialects.api

public interface CreationSqlGen {
    public fun column(name: String, type: String, nullable: Boolean, unique: String?): String
    public fun table(name: String, ifNotExists: Boolean, columns: List<String>, constraints: List<String>): String
}
