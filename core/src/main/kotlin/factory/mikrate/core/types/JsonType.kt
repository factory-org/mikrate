package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.models.DialectDbType

@Suppress("CanBeParameter")
public open class JsonType(public val preserveExact: Boolean) : DbType() {
    override val dialectDbType: DialectDbType = DialectDbType.JsonType(preserveExact)

    public companion object : JsonType(false)
}
