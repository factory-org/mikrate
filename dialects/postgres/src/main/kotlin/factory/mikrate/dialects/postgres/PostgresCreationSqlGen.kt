package factory.mikrate.dialects.postgres

import factory.mikrate.dialects.api.CreationSqlGen
import factory.mikrate.dialects.api.SupportStatus
import factory.mikrate.dialects.api.models.NewTable

public class PostgresCreationSqlGen(protected val typeGen: PostgresTypeSqlGen) : CreationSqlGen {
    private fun generateColumn(
        name: String,
        column: NewTable.Column
    ): String {
        var col = "$name ${typeGen.mapType(column.type)}"
        if (!column.nullable) {
            col += " not null"
        }
        if (column.unique != null) {
            col += " constraint ${column.unique} unique"
        }
        val foreign = column.foreign
        if (foreign != null) {
            col += " constraint ${foreign.constraintName}" +
                " foreign key references ${foreign.foreignTable}(${foreign.foreignColumn})"
        }
        return col
    }

    private fun generateConstraint(name: String, constraint: NewTable.Constraint): String = when (constraint) {
        is NewTable.Constraint.UniqueConstraint -> {
            "constraint $name unique (${constraint.columns.joinToString()})"
        }
        is NewTable.Constraint.ForeignKey -> {
            val localColumns = constraint.columnMapping.keys.joinToString()
            val foreignColumns = constraint.columnMapping.values.joinToString()
            "constraint $name foreign key ($localColumns) references ($foreignColumns)"
        }
    }

    public override fun table(newTable: NewTable): String {
        val columns = newTable.columns.map { generateColumn(it.key, it.value) }
        val constraints = newTable.constraints.map { generateConstraint(it.key, it.value) }
        val content = (columns + constraints).joinToString(",\n    ")
        //language=PostgreSQL
        return "create table ${newTable.name} (\n    $content\n);"
    }

    // TODO: Implement
    override fun tableSupported(newTable: NewTable): SupportStatus {
        return SupportStatus.Supported
    }

    private fun columnSupported(name: String, nullable: Boolean, unique: String?): SupportStatus =
        SupportStatus.Supported

    private fun uniqueSupported(name: String, columns: List<String>): SupportStatus = SupportStatus.Supported

    private fun tableSupported(name: String, ifNotExists: Boolean): SupportStatus = SupportStatus.Supported
}
