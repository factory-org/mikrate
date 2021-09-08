package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.models.DialectDbType

public open class IntegerType(public val size: Short = 4) : DbType() {
    override val dialectDbType: DialectDbType = DialectDbType.IntegerType(size)

    init {
        require(size > 0) { "\"size\" cannot be 0 or negative" }
    }

    public companion object : IntegerType()

    override fun toString(): String = "IntegerType(size = $size)"
}
