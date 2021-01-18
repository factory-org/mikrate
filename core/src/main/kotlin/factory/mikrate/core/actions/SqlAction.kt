package factory.mikrate.core.actions

import factory.mikrate.core.Dialect
import factory.mikrate.core.MigrateAction
import org.intellij.lang.annotations.Language

public class SqlAction(@Language("GenericSQL") private val script: String) : MigrateAction {
    override fun generateStatement(dialect: Dialect): String {
        return "$script;"
    }
}
