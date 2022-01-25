package factory.mikrate.dialects.h2

import factory.mikrate.dialects.api.CoreDialect

public object H2CoreDialect : CoreDialect {
    override val id: String = "h2"
    public override val types: H2TypeSqlGen = H2TypeSqlGen()
    override val constraints: H2ConstraintSqlGen = H2ConstraintSqlGen
    override val creation: H2CreationSqlGen = H2CreationSqlGen(types)
    override val alter: H2AlterSqlGen = H2AlterSqlGen()
}
