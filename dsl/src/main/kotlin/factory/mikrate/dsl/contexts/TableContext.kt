package factory.mikrate.dsl.contexts

import factory.mikrate.core.MigrateAction
import factory.mikrate.dsl.MigrationDsl

@MigrationDsl
public class TableContext(public val name: String) {
    public val actions: MutableList<MigrateAction> = mutableListOf()
}
