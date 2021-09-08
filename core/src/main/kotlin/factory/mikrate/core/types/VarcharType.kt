package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.models.DialectDbType

public class VarcharType(public val length: Int) : DbType() {
    override val dialectDbType: DialectDbType = DialectDbType.VarcharType(length)

    init {
        require (length > 0) { "\"length\" cannot be 0 or negative" }
    }
}
