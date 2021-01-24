package factory.mikrate.executors.api

public sealed class MigrationResult {
    public object Success : MigrationResult()
    public class Error(public val error: Any? = null) : MigrationResult()
}
