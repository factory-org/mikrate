package factory.mikrate.dialects.h2

import factory.mikrate.dialects.api.CreationSqlGen
import factory.mikrate.dialects.api.SupportStatus
import factory.mikrate.dialects.api.models.NewEnum
import factory.mikrate.dialects.api.models.NewTable

public class H2CreationSqlGen(private val typeGen: H2TypeSqlGen, public val quoteIdentifiers: Boolean) :
    CreationSqlGen {
    private fun generateColumn(
        name: String,
        column: NewTable.Column
    ): String {
        val quotedName = if (quoteIdentifiers) "\"$name\"" else name
        val quotedUnique = if (quoteIdentifiers) "\"${column.unique}\"" else column.unique
        var col = "$quotedName ${typeGen.mapType(column.type)}"
        if (!column.nullable) {
            col += " not null"
        }
        if (column.primary) {
            col += " primary key"
        }
        if (column.unique != null) {
            col += " constraint $quotedUnique unique"
        }
        return col
    }

    private fun generateForeignKeyConstraint(
        name: String,
        foreignTable: String,
        columnMapping: Map<String, String>
    ): String {
        val quotedName = if (quoteIdentifiers) "\"$name\"" else name
        val localColumns = columnMapping.keys.joinToString { if (quoteIdentifiers) "\"$it\"" else it }
        val foreignColumns = columnMapping.values.joinToString { if (quoteIdentifiers) "\"$it\"" else it }
        val quotedForeignTable = if (quoteIdentifiers) "\"$foreignTable\"" else foreignTable
        //language=H2
        return "constraint $quotedName foreign key ($localColumns) references $quotedForeignTable ($foreignColumns)"
    }

    private fun generateConstraint(name: String, constraint: NewTable.Constraint): String = when (constraint) {
        is NewTable.Constraint.UniqueConstraint -> {
            val quotedName = if (quoteIdentifiers) "\"$name\"" else name
            val columns = constraint.columns.joinToString { if (quoteIdentifiers) "\"$it\"" else it }
            //language=H2
            "constraint $quotedName unique ($columns)"
        }
        is NewTable.Constraint.ForeignKey -> generateForeignKeyConstraint(
            name,
            constraint.foreignTable,
            constraint.columnMapping
        )
    }

    private fun generateCompositePrimaryKey(columns: Set<String>): String {
        return "primary key (${columns.joinToString()}))"
    }

    public override fun table(newTable: NewTable): String {
        val columns = newTable.columns.map { generateColumn(it.key, it.value) }
        val foreignConstraints = newTable.columns
            .mapNotNull {
                val (name, column) = it
                val foreign = column.foreign ?: return@mapNotNull null
                generateForeignKeyConstraint(
                    foreign.constraintName,
                    foreign.foreignTable,
                    mapOf(name to foreign.foreignColumn)
                )
            }
        val constraints = newTable.constraints.map { generateConstraint(it.key, it.value) }
        val contentList = (columns + foreignConstraints + constraints).toMutableList()
        val compositePrimaryKey = newTable.compositePrimaryKey
        if (compositePrimaryKey != null) {
            contentList.add(generateCompositePrimaryKey(compositePrimaryKey))
        }
        val content = contentList.joinToString(",\n    ")
        val quotedName = if (quoteIdentifiers) "\"${newTable.name}\"" else newTable.name
        //language=H2
        return "create table $quotedName (\n    $content\n);"
    }

    // TODO: Implement
    override fun tableSupported(newTable: NewTable): SupportStatus {
        return SupportStatus.Supported
    }

    override fun enum(newEnum: NewEnum): String {
        val values = newEnum.values.joinToString { "'$it'" }
        val quotedName = if (quoteIdentifiers) "\"${newEnum.name}\"" else newEnum.name
        //language=H2
        return "create domain $quotedName as enum ($values);"
    }

    override fun enumSupported(newEnum: NewEnum): SupportStatus = SupportStatus.Supported

    private fun columnSupported(name: String, nullable: Boolean, unique: String?): SupportStatus =
        SupportStatus.Supported

    private fun uniqueSupported(name: String, columns: List<String>): SupportStatus = SupportStatus.Supported

    private fun tableSupported(name: String, ifNotExists: Boolean): SupportStatus = SupportStatus.Supported
}
