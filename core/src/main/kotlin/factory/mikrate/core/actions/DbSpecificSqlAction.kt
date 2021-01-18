package factory.mikrate.core.actions

import factory.mikrate.core.Dialect
import factory.mikrate.core.MigrateAction

public class DbSpecificSqlAction(private val mapping: Map<Dialect, String?>) : MigrateAction {
    override fun generateStatement(dialect: Dialect): String? {
        if (!mapping.containsKey(dialect)) {
            throw NotImplementedError("No sql statement available for dialect $dialect")
        }
        return mapping[dialect]
    }
}
