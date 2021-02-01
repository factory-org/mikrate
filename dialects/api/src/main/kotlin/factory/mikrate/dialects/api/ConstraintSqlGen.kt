package factory.mikrate.dialects.api

public interface ConstraintSqlGen {
    public fun unique(name: String, columns: List<String>): String
}