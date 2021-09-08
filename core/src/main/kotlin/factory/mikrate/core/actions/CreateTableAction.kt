package factory.mikrate.core.actions

import factory.mikrate.core.MigrateAction
import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.dialects.api.SupportStatus
import factory.mikrate.dialects.api.models.NewTable

public class CreateTableAction(
    public val name: String,
    public val columns: Map<String, NewTable.Column>,
    public val constraints: Map<String, NewTable.Constraint>,
    public val ifNotExists: Boolean = false
) : MigrateAction {
    override fun generateStatement(dialect: CoreDialect): String {
        return dialect.creation.table(newTable())
    }

    override fun isSupported(dialect: CoreDialect): SupportStatus {
        return dialect.creation.tableSupported(newTable())
    }

    private fun newTable(): NewTable = NewTable(name, columns, constraints, ifNotExists)
}
