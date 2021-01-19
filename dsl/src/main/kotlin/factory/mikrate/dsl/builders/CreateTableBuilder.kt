package factory.mikrate.dsl.builders

import factory.mikrate.core.DbType
import factory.mikrate.core.actions.CreateTableAction
import factory.mikrate.dsl.MigrationDsl
import factory.mikrate.dsl.helpers.ColumnRef

@MigrationDsl
public class CreateTableBuilder(public val tableName: String) {
    private val columns: MutableMap<String, CreateTableAction.NewColumn> = mutableMapOf()
    private val constraints: MutableMap<String, CreateTableAction.Constraint> = mutableMapOf()

    public fun column(name: String, type: DbType, nullable: Boolean = false): ColumnRef {
        columns[name] = CreateTableAction.NewColumn(type, nullable, null)
        return ColumnRef(tableName, name)
    }

    public fun column(name: String, type: DbType, block: ColumnConfigurer.() -> Unit): ColumnRef {
        val configurer = ColumnConfigurer(tableName, name)
        block(configurer)
        val config = configurer.config()
        val unique = if (config.unique) config.uniqueName else null
        columns[name] = CreateTableAction.NewColumn(type, config.nullable, unique)
        return ColumnRef(tableName, name)
    }

    public fun unique(name: String, vararg columns: ColumnRef) {
        constraints[name] = CreateTableAction.UniqueConstraint(columns.map { it.column })
    }

    public fun unique(vararg columns: ColumnRef) {
        val name = "uix_${tableName}_${columns.joinToString("_") { it.column }}"
        unique(name, columns = columns)
    }

    public fun build(): CreateTableAction {
        return CreateTableAction(tableName, columns, constraints)
    }
}
