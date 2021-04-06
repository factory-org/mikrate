package factory.mikrate.core.actions

import factory.mikrate.core.MigrateAction
import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.dialects.api.SupportStatus
import org.intellij.lang.annotations.Language

public class SqlAction(
    @Language("GenericSQL")
    private val script: String
) : MigrateAction {
    override fun generateStatement(dialect: CoreDialect): String {
        return "$script;"
    }

    override fun isSupported(dialect: CoreDialect): SupportStatus = SupportStatus.Supported
}
