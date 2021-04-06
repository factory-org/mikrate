package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.*

public object GenericCoreDialect : CoreDialect {
    override val id: String = "generic"
    override val types: TypeSqlGen = GenericTypeSqlGen
    override val constraints: ConstraintSqlGen = GenericConstraintSqlGen
    override val creation: CreationSqlGen = GenericCreationSqlGen
    override val alter: AlterSqlGen = GenericAlterSqlGen
}
