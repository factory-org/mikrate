package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.ConstraintSqlGen

public object GenericConstraintSqlGen : ConstraintSqlGen {
    override fun unique(name: String, columns: List<String>): String {
        //language=GenericSQL
        return "constraint $name unique (${columns.joinToString()})"
    }
}
