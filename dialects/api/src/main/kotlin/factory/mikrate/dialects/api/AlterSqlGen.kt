package factory.mikrate.dialects.api

public interface AlterSqlGen {
    public fun renameColumn(table: String, column: String, to: String): String?
    public fun renameColumnSupported(table: String, column: String, newName: String): SupportStatus
}
