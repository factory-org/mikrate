package factory.mikrate.dialects.sqlite

import factory.mikrate.dialects.api.AlterSqlGen
import factory.mikrate.dialects.api.ConstraintSqlGen
import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.dialects.api.CreationSqlGen
import factory.mikrate.dialects.api.TypeSqlGen

public object SqliteCoreDialect : CoreDialect {
    override val id: String = "sqlite"
    override val types: TypeSqlGen = SqliteTypeSqlGen
    override val constraints: ConstraintSqlGen = SqliteConstraintSqlGen
    override val creation: CreationSqlGen = SqliteCreationSqlGen
    override val alter: AlterSqlGen = SqliteAlterSqlGen
}
