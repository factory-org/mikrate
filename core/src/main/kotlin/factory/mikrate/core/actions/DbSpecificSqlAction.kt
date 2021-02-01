package factory.mikrate.core.actions

import factory.mikrate.core.MigrateAction
import factory.mikrate.dialects.api.CoreDialect

public class DbSpecificSqlAction(private val mapping: Map<String, String?>) : MigrateAction {
    override fun generateStatement(dialect: CoreDialect): String? {
        if (!mapping.containsKey(dialect.id)) {
            throw NotImplementedError("No sql statement available for dialect ${dialect.id}")
        }
        return mapping[dialect.id]
    }
}
