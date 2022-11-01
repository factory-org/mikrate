package factory.mikrate.dialects.h2

import factory.mikrate.dialects.api.CoreDialect

public open class H2CoreDialect(public val quoteIdentifiers: Boolean = true) : CoreDialect {
    override val id: String = "h2"
    public final override val types: H2TypeSqlGen = H2TypeSqlGen(quoteIdentifiers)
    override val constraints: H2ConstraintSqlGen = H2ConstraintSqlGen
    override val creation: H2CreationSqlGen = H2CreationSqlGen(types, quoteIdentifiers)
    override val alter: H2AlterSqlGen = H2AlterSqlGen(quoteIdentifiers)

    public companion object : H2CoreDialect()
}
