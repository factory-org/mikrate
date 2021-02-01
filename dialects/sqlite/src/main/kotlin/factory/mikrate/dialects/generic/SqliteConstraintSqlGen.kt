package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.ConstraintSqlGen

public object SqliteConstraintSqlGen : ConstraintSqlGen {
    override fun unique(name: String, columns: List<String>): String {
        //language=SQLite
        return "constraint $name unique (${columns.joinToString()})"
    }
}
