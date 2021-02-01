package factory.mikrate.dialects.postgres

import factory.mikrate.dialects.api.ConstraintSqlGen

public object PostgresConstraintSqlGen : ConstraintSqlGen {
    override fun unique(name: String, columns: List<String>): String {
        //language=PostgreSQL
        return "constraint $name unique (${columns.joinToString()})"
    }
}
