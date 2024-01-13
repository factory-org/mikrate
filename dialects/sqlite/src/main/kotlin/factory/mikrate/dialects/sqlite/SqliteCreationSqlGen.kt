package factory.mikrate.dialects.sqlite

import factory.mikrate.dialects.api.CreationSqlGen
import factory.mikrate.dialects.api.SupportStatus
import factory.mikrate.dialects.api.models.NewEnum
import factory.mikrate.dialects.api.models.NewTable
import factory.mikrate.dialects.sqlite.SqliteTypeSqlGen.mapType

public object SqliteCreationSqlGen : CreationSqlGen {
    private fun generateColumn(
        name: String,
        column: NewTable.Column
    ): String {
        var col = "$name ${mapType(column.type)}"
        if (!column.nullable) {
            col += " not null"
        }
        if (column.primary) {
            col += " primary key"
        }
        if (column.unique != null) {
            col += " constraint ${column.unique} unique"
        }
        return col
    }

    private fun generateForeignKeyConstraint(foreignTable: String, columnMapping: Map<String, String>): String {
        val localColumns = columnMapping.keys.joinToString()
        val foreignColumns = columnMapping.values.joinToString()
        //language=SQLite
        return "foreign key ($localColumns) references $foreignTable ($foreignColumns)"
    }

    private fun generateConstraint(name: String, constraint: NewTable.Constraint): String = when (constraint) {
        is NewTable.Constraint.UniqueConstraint -> {
            //language=SQLite
            "constraint $name unique (${constraint.columns.joinToString()})"
        }
        is NewTable.Constraint.ForeignKey -> generateForeignKeyConstraint(
            constraint.foreignTable,
            constraint.columnMapping
        )
    }

    private fun generateCompositePrimaryKey(columns: Set<String>): String {
        //language=SQLite
        return "primary key (${columns.joinToString()})"
    }

    public override fun table(newTable: NewTable): String {
        val columns = newTable.columns.map { generateColumn(it.key, it.value) }
        val foreignConstraints = newTable.columns
            .mapNotNull {
                val (name, column) = it
                val foreign = column.foreign ?: return@mapNotNull null
                generateForeignKeyConstraint(foreign.foreignTable, mapOf(name to foreign.foreignColumn))
            }
        val constraints = newTable.constraints.map { generateConstraint(it.key, it.value) }
        val contentList = (columns + foreignConstraints + constraints).toMutableList()
        val compositePrimaryKey = newTable.compositePrimaryKey
        if (compositePrimaryKey != null) {
            contentList.add(generateCompositePrimaryKey(compositePrimaryKey))
        }
        val content = contentList.joinToString(",\n    ")
        //language=SQLite
        return "CREATE TABLE ${newTable.name} (\n    $content\n);"
    }

    // TODO: Implement
    override fun tableSupported(newTable: NewTable): SupportStatus {
        return SupportStatus.Supported
    }

    override fun enum(newEnum: NewEnum): String = throw UnsupportedOperationException("Enums not supported in SQLite")

    override fun enumSupported(newEnum: NewEnum): SupportStatus = SupportStatus.Unsupported()

    private fun columnSupported(name: String, nullable: Boolean, unique: String?): SupportStatus =
        SupportStatus.Supported

    private fun uniqueSupported(name: String, columns: List<String>): SupportStatus = SupportStatus.Supported

    private fun tableSupported(name: String, ifNotExists: Boolean): SupportStatus = SupportStatus.Supported
}
