package factory.mikrate.core.actions

import factory.mikrate.core.MigrateAction
import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.dialects.api.SupportStatus

public class RenameColumnAction(public val table: String, public val column: String, public val newName: String) :
    MigrateAction {
    override fun generateStatement(dialect: CoreDialect): String? = dialect.alter.renameColumn(table, column, newName)

    override fun isSupported(dialect: CoreDialect): SupportStatus =
        dialect.alter.renameColumnSupported(table, column, newName)
}
