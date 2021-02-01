package factory.mikrate.dialects.generic

import factory.mikrate.dialects.api.CreationSqlGen

public object GenericCreationSqlGen : CreationSqlGen {
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
        //language=GenericSQL
        return "CREATE TABLE $name (\n    $content\n);"
    }
}