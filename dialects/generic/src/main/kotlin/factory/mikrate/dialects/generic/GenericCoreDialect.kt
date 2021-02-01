package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.ConstraintSqlGen
import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.dialects.api.CreationSqlGen
import factory.mikrate.dialects.api.TypeSqlGen

public object GenericCoreDialect : CoreDialect {
    override val id: String = "generic"
    override val types: TypeSqlGen = GenericTypeSqlGen
    override val constraints: ConstraintSqlGen = GenericConstraintSqlGen
    override val creation: CreationSqlGen = GenericCreationSqlGen
}
