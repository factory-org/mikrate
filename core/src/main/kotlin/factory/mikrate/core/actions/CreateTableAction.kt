package factory.mikrate.core.actions

import factory.mikrate.core.DbType
import factory.mikrate.core.Dialect
import factory.mikrate.core.MigrateAction

public class CreateTableAction(
    public val name: String,
    public val columns: Map<String, NewColumn>,
    public val ifNotExists: Boolean = false
) : MigrateAction {
    public data class NewColumn(
        public val type: DbType,
        public val nullable: Boolean = false,
        public val unique: String?
    )

    override fun generateStatement(dialect: Dialect): String {
        return when (dialect) {
            Dialect.Postgres,
            Dialect.Sqlite -> {
                val colSpec = columns.map {
                    val (name, cfg) = it
                    var sql = "$name ${cfg.type.toSql(dialect)}"
                    if (cfg.nullable) {
                        sql += " NOT NULL"
                    }
                    if (cfg.unique != null) {
                        sql += " constraint ${cfg.unique} unique"
                    }
                    sql
                }.joinToString(",\n")
                //language=GenericSQL
                """
                    CREATE TABLE $name (
                        $colSpec
                    );
                """.trimIndent()
            }
        }
    }
}
