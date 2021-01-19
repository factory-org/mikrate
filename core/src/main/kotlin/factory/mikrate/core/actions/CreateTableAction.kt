package factory.mikrate.core.actions

import factory.mikrate.core.DbType
import factory.mikrate.core.Dialect
import factory.mikrate.core.MigrateAction

public class CreateTableAction(
    public val name: String,
    public val columns: Map<String, NewColumn>,
    public val constraints: Map<String, Constraint>,
    public val ifNotExists: Boolean = false
) : MigrateAction {
    public data class NewColumn(
        public val type: DbType,
        public val nullable: Boolean = false,
        public val unique: String?
    )

    public interface Constraint {
        public fun toSql(dialect: Dialect): String?
    }

    public class UniqueConstraint(private val columns: List<String>) : Constraint {
        override fun toSql(dialect: Dialect): String {
            return when (dialect) {
                Dialect.Postgres,
                Dialect.Sqlite -> {
                    //language=GenericSQL
                    "unique (${columns.joinToString(", ")})"
                }
            }
        }
    }

    override fun generateStatement(dialect: Dialect): String {
        return when (dialect) {
            Dialect.Postgres,
            Dialect.Sqlite -> {
                val colStrings = columns.map {
                    val (name, cfg) = it
                    //language=GenericSQL
                    var sql = "$name ${cfg.type.toSql(dialect)}"
                    if (!cfg.nullable) {
                        //language=GenericSQL
                        sql += " NOT NULL"
                    }
                    if (cfg.unique != null) {
                        //language=GenericSQL
                        sql += " constraint ${cfg.unique} unique"
                    }
                    sql
                }
                val constrStrings = constraints.map {
                    val (name, constraint) = it
                    //language=GenericSQL
                    "constraint $name ${constraint.toSql(dialect)}"
                }
                val body = (colStrings + constrStrings).joinToString(",\n    ")
                //language=GenericSQL
                "CREATE TABLE $name (\n    $body\n);".trimIndent()
            }
        }
    }
}
