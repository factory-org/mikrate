package factory.mikrate.core.types

import factory.mikrate.core.DbType
import factory.mikrate.dialects.api.models.DialectDbType

public class LongType : DbType() {
    override val dialectDbType: DialectDbType = DialectDbType.IntegerType(8)
}
