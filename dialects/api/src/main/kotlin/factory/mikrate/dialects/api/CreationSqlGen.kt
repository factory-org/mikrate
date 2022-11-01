package factory.mikrate.dialects.api

import factory.mikrate.dialects.api.models.NewEnum
import factory.mikrate.dialects.api.models.NewTable

public interface CreationSqlGen {
    public fun table(newTable: NewTable): String
    public fun tableSupported(newTable: NewTable): SupportStatus
    public fun enum(newEnum: NewEnum): String
    public fun enumSupported(newEnum: NewEnum): SupportStatus
}
