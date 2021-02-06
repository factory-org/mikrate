package factory.mikrate.dsl.builders

import factory.mikrate.core.MigrateAction
import factory.mikrate.core.actions.DbSpecificSqlAction
import factory.mikrate.core.actions.SqlAction
import factory.mikrate.dsl.MigrationDsl
import factory.mikrate.dsl.contexts.TableContext
import org.intellij.lang.annotations.Language

@MigrationDsl
public class ActionBuilder {
    public val actions: MutableList<MigrateAction> = mutableListOf()

    public fun sql(@Language("SQL") script: String) {
        actions.add(SqlAction(script))
    }

    public fun sql(block: DbSpecificSqlBuilder.() -> Unit) {
        val builder = DbSpecificSqlBuilder()
        block(builder)
        val mapping = builder.build()
        actions.add(DbSpecificSqlAction(mapping))
    }

    public fun createTable(name: String, block: CreateTableBuilder.() -> Unit) {
        val builder = CreateTableBuilder(name)
        block(builder)
        actions.add(builder.build())
    }

    public fun table(name: String, block: TableContext.() -> Unit) {
        val context = TableContext(name)
        block(context)
        actions.addAll(context.actions)
    }
}
