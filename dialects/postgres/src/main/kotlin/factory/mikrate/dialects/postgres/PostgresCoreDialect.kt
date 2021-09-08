package factory.mikrate.dialects.postgres

import factory.mikrate.dialects.api.CoreDialect

public open class PostgresCoreDialect(public val version: PostgresVersion) : CoreDialect {
    override val id: String = "postgres"
    public override val types: PostgresTypeSqlGen = PostgresTypeSqlGen(version)
    override val constraints: PostgresConstraintSqlGen = PostgresConstraintSqlGen
    @Suppress("LeakingThis")
    override val creation: PostgresCreationSqlGen = PostgresCreationSqlGen(types)
    override val alter: PostgresAlterSqlGen = PostgresAlterSqlGen(version)

    public companion object : PostgresCoreDialect(PostgresVersion.Unknown) {
        public val v13: PostgresCoreDialect
            get() = PostgresCoreDialect(PostgresVersion.V13)
    }
}
