package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.*

public object SqliteCoreDialect : CoreDialect {
    override val id: String = "sqlite"
    override val types: TypeSqlGen = SqliteTypeSqlGen
    override val constraints: ConstraintSqlGen = SqliteConstraintSqlGen
    override val creation: CreationSqlGen = SqliteCreationSqlGen
    override val alter: AlterSqlGen = SqliteAlterSqlGen
}
