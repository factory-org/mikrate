package factory.mikrate.core.actions

import factory.mikrate.core.MigrateAction
import factory.mikrate.dialects.api.CoreDialect
import factory.mikrate.dialects.api.SupportStatus
import factory.mikrate.dialects.api.models.NewEnum

public class CreateEnumAction(
    public val name: String,
    public val values: List<String>,
) : MigrateAction {
    override fun generateStatement(dialect: CoreDialect): String {
        return dialect.creation.enum(newEnum())
    }

    override fun isSupported(dialect: CoreDialect): SupportStatus {
        return dialect.creation.enumSupported(newEnum())
    }

    private fun newEnum(): NewEnum = NewEnum(name, values)
}
