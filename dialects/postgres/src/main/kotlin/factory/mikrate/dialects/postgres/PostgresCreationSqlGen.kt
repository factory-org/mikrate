package factory.mikrate.dialects.postgres

import factory.mikrate.dialects.api.CreationSqlGen
import factory.mikrate.dialects.api.SupportStatus

public object PostgresCreationSqlGen : CreationSqlGen {
    override fun column(name: String, type: String, nullable: Boolean, unique: String?): String {
        var col = "$name $type"
        if (!nullable) {
            col += " NOT NULL"
        }
        if (unique != null) {
            col += " constraint $unique unique"
        }
        return col
    }

    override fun table(name: String, ifNotExists: Boolean, columns: List<String>, constraints: List<String>): String {
        val content = (columns + constraints).joinToString(",\n    ")
        //language=PostgreSQL
        return "CREATE TABLE $name (\n    $content\n);"
    }

    override fun columnSupported(name: String, nullable: Boolean, unique: String?): SupportStatus =
        SupportStatus.Supported

    override fun uniqueSupported(name: String, columns: List<String>): SupportStatus = SupportStatus.Supported

    override fun tableSupported(name: String, ifNotExists: Boolean): SupportStatus = SupportStatus.Supported
}
