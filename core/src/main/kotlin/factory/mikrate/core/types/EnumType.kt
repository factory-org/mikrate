package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.models.DialectDbType

public class EnumType(public val name: String) : DbType() {
    override val dialectDbType: DialectDbType = DialectDbType.EnumType(name)

    init {
        require (name.isNotBlank()) { "\"name\" cannot be blank" }
    }
}
