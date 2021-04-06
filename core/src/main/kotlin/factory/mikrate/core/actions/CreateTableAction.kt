package factory.mikrate.core.actions

import factory.mikrate.core.DbType
import factory.mikrate.core.MigrateAction
import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.dialects.api.SupportStatus

public class CreateTableAction(
    public val name: String,
    public val columns: Map<String, NewColumn>,
    public val constraints: Map<String, Constraint>,
    public val ifNotExists: Boolean = false
) : MigrateAction {
    public data class NewColumn(
        public val type: DbType, public val nullable: Boolean = false, public val unique: String?
    )

    public sealed class Constraint {
        public class UniqueConstraint(public val columns: List<String>) : Constraint()
    }

    override fun generateStatement(dialect: CoreDialect): String {
        val columns = columns.map {
            val (name, cfg) = it
            dialect.creation.column(name, cfg.type.toSql(dialect.types), cfg.nullable, cfg.unique)
        }
        val constraints = constraints.map {
            val (name, constraint) = it
            when (constraint) {
                is Constraint.UniqueConstraint -> dialect.constraints.unique(name, constraint.columns)
            }
        }
        return dialect.creation.table(name, ifNotExists, columns, constraints)
    }

    override fun isSupported(dialect: CoreDialect): SupportStatus {
        for ((name, cfg) in columns) {
            if (!cfg.type.supports(dialect.types)) {
                return SupportStatus.Unsupported()
            }
            return dialect.creation.columnSupported(name, cfg.nullable, cfg.unique)
        }
        for ((name, constraint) in constraints) {
            when (constraint) {
                is Constraint.UniqueConstraint -> return dialect.creation.uniqueSupported(name, constraint.columns)
            }
        }
        return dialect.creation.tableSupported(name, ifNotExists)
    }
}
