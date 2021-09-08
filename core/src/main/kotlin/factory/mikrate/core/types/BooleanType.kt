package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.models.DialectDbType

public object BooleanType : DbType() {
    override val dialectDbType: DialectDbType = DialectDbType.BooleanType
}
