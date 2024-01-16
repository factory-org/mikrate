package factory.mikrate.dialects.sqlite

import factory.mikrate.dialects.api.*

public open class SqliteCoreDialect(protected val options: SqliteDialectOptions = SqliteDialectOptions()) : CoreDialect {
    override val id: String = "sqlite"
    override val types: TypeSqlGen = SqliteTypeSqlGen
    override val constraints: ConstraintSqlGen = SqliteConstraintSqlGen
    override val creation: CreationSqlGen = SqliteCreationSqlGen(options)
    override val alter: AlterSqlGen = SqliteAlterSqlGen

    public companion object : SqliteCoreDialect()
}
