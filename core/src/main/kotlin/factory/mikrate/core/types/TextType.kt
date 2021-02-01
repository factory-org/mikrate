package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.TypeSqlGen

public class TextType : DbType {
    override fun supports(dialect: TypeSqlGen): Boolean = dialect.supportsText()

    override fun toSql(dialect: TypeSqlGen): String = dialect.text()
}
