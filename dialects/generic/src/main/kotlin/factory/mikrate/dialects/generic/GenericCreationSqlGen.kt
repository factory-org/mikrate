package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.CreationSqlGen
import factory.mikrate.dialects.api.SupportStatus
import factory.mikrate.dialects.api.models.NewEnum
import factory.mikrate.dialects.api.models.NewTable
import factory.mikrate.dialects.generic.GenericTypeSqlGen.mapType

public object GenericCreationSqlGen : CreationSqlGen {
    private fun generateColumn(
        name: String,
        column: NewTable.Column
    ): String {
        var col = "$name ${mapType(column.type)}"
        if (!column.nullable) {
            col += " NOT NULL"
        }
        if (column.primary) {
            col += " PRIMARY KEY"
        }
        if (column.unique != null) {
            col += " CONSTRAINT ${column.unique} UNIQUE"
        }
        val foreign = column.foreign
        if (foreign != null) {
            col += " CONSTRAINT ${foreign.constraintName}" +
                " FOREIGN KEY REFERENCES ${foreign.foreignTable}(${foreign.foreignColumn})"
        }
        return col
    }

    private fun generateConstraint(name: String, constraint: NewTable.Constraint): String = when (constraint) {
        is NewTable.Constraint.UniqueConstraint -> {
            "CONSTRAINT $name UNIQUE (${constraint.columns.joinToString()})"
        }
        is NewTable.Constraint.ForeignKey -> {
            val localColumns = constraint.columnMapping.keys.joinToString()
            val foreignColumns = constraint.columnMapping.values.joinToString()
            "CONSTRAINT $name FOREIGN KEY ($localColumns) REFERENCES ($foreignColumns)"
        }
    }

    public override fun table(newTable: NewTable): String {
        val columns = newTable.columns.map { generateColumn(it.key, it.value) }
        val constraints = newTable.constraints.map { generateConstraint(it.key, it.value) }
        val content = (columns + constraints).joinToString(",\n    ")
        //language=GenericSQL
        return "CREATE TABLE ${newTable.name} (\n    $content\n);"
    }

    // TODO: Implement
    override fun tableSupported(newTable: NewTable): SupportStatus = SupportStatus.Supported

    override fun enum(newEnum: NewEnum): String {
        val values = newEnum.values.joinToString { "'$it'" }
        //language=GenericSQL
        return "create type \"${newEnum.name}\" as enum ($values);"
    }

    override fun enumSupported(newEnum: NewEnum): SupportStatus = SupportStatus.Supported

    private fun columnSupported(name: String, nullable: Boolean, unique: String?): SupportStatus =
        SupportStatus.Supported

    private fun uniqueSupported(name: String, columns: List<String>): SupportStatus = SupportStatus.Supported

    private fun tableSupported(name: String, ifNotExists: Boolean): SupportStatus = SupportStatus.Supported
}
