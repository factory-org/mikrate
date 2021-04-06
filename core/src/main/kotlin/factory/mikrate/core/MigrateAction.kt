package factory.mikrate.core

import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.dialects.api.SupportStatus

public interface MigrateAction {
    /**
     * If it returns `null`, nothing will be executed.
     */
    public fun generateStatement(dialect: CoreDialect): String?

    /**
     * Returns whether this action is supported on the current dialect
     */
    public fun isSupported(dialect: CoreDialect): SupportStatus
}
