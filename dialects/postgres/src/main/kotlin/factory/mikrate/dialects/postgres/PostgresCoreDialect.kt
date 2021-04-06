package factory.mikrate.dialects.postgres

import factory.mikrate.dialects.api.*

public open class PostgresCoreDialect(public val version: PostgresVersion) : CoreDialect {
    override val id: String = "postgres"
    public override val types: TypeSqlGen = PostgresTypeSqlGen(version)
    override val constraints: ConstraintSqlGen = PostgresConstraintSqlGen
    override val creation: CreationSqlGen = PostgresCreationSqlGen
    override val alter: AlterSqlGen = PostgresAlterSqlGen(version)

    public companion object : PostgresCoreDialect(PostgresVersion.Unknown) {
        public val v13: PostgresCoreDialect
            get() = PostgresCoreDialect(PostgresVersion.V13)
    }
}
