package factory.mikrate.dialects.h2

import factory.mikrate.dialects.api.CreationSqlGen
import factory.mikrate.dialects.api.SupportStatus
import factory.mikrate.dialects.api.models.NewTable

public class H2CreationSqlGen(protected val typeGen: H2TypeSqlGen) : CreationSqlGen {
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
        return col
    }

    private fun generateForeignKeyConstraint(name: String, foreignTable: String, columnMapping: Map<String, String>): String {
        val localColumns = columnMapping.keys.joinToString()
        val foreignColumns = columnMapping.values.joinToString()
        //language=H2
        return "foreign key $name ($localColumns) references $foreignTable ($foreignColumns)"
    }

    private fun generateConstraint(name: String, constraint: NewTable.Constraint): String = when (constraint) {
        is NewTable.Constraint.UniqueConstraint -> {
            //language=H2
            "unique key $name (${constraint.columns.joinToString()})"
        }
        is NewTable.Constraint.ForeignKey -> generateForeignKeyConstraint(
            name,
            constraint.foreignTable,
            constraint.columnMapping
        )
    }

    public override fun table(newTable: NewTable): String {
        val columns = newTable.columns.map { generateColumn(it.key, it.value) }
        val foreignConstraints = newTable.columns
            .mapNotNull {
                val (name, column) = it
                val foreign = column.foreign ?: return@mapNotNull null
                generateForeignKeyConstraint(foreign.constraintName, foreign.foreignTable, mapOf(name to foreign.foreignColumn))
            }
        val constraints = newTable.constraints.map { generateConstraint(it.key, it.value) }
        val content = (columns + foreignConstraints + constraints).joinToString(",\n    ")
        //language=H2
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
