package factory.mikrate.dsl.builders

import factory.mikrate.core.MigrateAction
import factory.mikrate.core.actions.CreateEnumAction
import factory.mikrate.core.actions.DbSpecificSqlAction
import factory.mikrate.core.actions.RenameColumnAction
import factory.mikrate.core.actions.SqlAction
import factory.mikrate.core.types.EnumType
import factory.mikrate.dsl.MigrationDsl
import factory.mikrate.dsl.contexts.TableContext
import factory.mikrate.dsl.helpers.ColumnRef
import factory.mikrate.dsl.helpers.TableRef
import org.intellij.lang.annotations.Language
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Builder for building the individual steps of a migrations.
 *
 * @see MigrateAction
 */
@MigrationDsl
public class ActionBuilder {
    /**
     * List that holds all the individual steps of the migration.
     */
    public val actions: MutableList<MigrateAction> = mutableListOf()

    /**
     * Executes the given SQL string regardless of the database dialect.
     *
     * @see SqlAction
     */
    public fun sql(@Language("GenericSQL") script: String) {
        actions.add(SqlAction(script))
    }

    /**
     * Allows specifying SQL strings that will be executed for each dialect differently.
     *
     * @see DbSpecificSqlAction
     * @see DbSpecificSqlBuilder
     */
    public fun sql(block: DbSpecificSqlBuilder.() -> Unit) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val builder = DbSpecificSqlBuilder()
        block(builder)
        val mapping = builder.build()
        actions.add(DbSpecificSqlAction(mapping))
    }

    /**
     * Creates a new table.
     *
     * @see factory.mikrate.core.actions.CreateTableAction
     * @see CreateTableBuilder
     */
    public fun createTable(name: String, block: CreateTableBuilder.() -> Unit): TableRef {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val builder = CreateTableBuilder(name)
        block(builder)
        actions.add(builder.build())
        return TableRef(name)
    }

    /**
     * Moves execution into the context of a table. From that context various action on the table can be executed.
     *
     * @see TableContext
     */
    public fun table(name: String, block: TableContext.() -> Unit) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val context = TableContext(name)
        block(context)
        actions.addAll(context.actions)
    }

    /**
     * Returns a reference to the specified table.
     *
     * @param name Name of the table referenced.
     */
    public fun table(name: String): TableRef = TableRef(name)

    /**
     * Creates a new enum type.
     *
     * @param name Name of the enum type to be created.
     * @param values Possible values for the enums.
     */
    public fun createEnum(name: String, values: List<String>): EnumType {
        check(name.isNotBlank()) { "Enum name has to not be blank" }
        check(values.isNotEmpty()) { "Enum has to have at least one value" }
        actions.add(CreateEnumAction(name, values))
        return EnumType(name)
    }
    /**
     * Creates a new enum type.
     *
     * @param name Name of the enum type to be created.
     * @param values Possible values for the enums.
     */
    public fun createEnum(name: String, vararg values: String): EnumType {
        return createEnum(name, values.asList())
    }

    /**
     * Renames a column.
     */
    public fun ColumnRef.renameTo(to: String) {
        actions.add(RenameColumnAction(table, column, to))
    }
}
