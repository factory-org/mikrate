package factory.mikrate.core

import factory.mikrate.dialects.api.CoreDialect

public interface MigrateAction {
    /**
     * If it returns `null`, nothing will be executed.
     */
    public fun generateStatement(dialect: CoreDialect): String?
}
