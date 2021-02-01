package factory.mikrate.dialects.postgres

import factory.mikrate.dialects.api.ConstraintSqlGen
import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.dialects.api.CreationSqlGen
import factory.mikrate.dialects.api.TypeSqlGen

public object PostgresCoreDialect : CoreDialect {
    override val id: String = "postgres"
    public override val types: TypeSqlGen = PostgresTypeSqlGen
    override val constraints: ConstraintSqlGen = PostgresConstraintSqlGen
    override val creation: CreationSqlGen = PostgresCreationSqlGen
}
