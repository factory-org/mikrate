package factory.mikrate.dialects.api.models

public data class NewTable(
    public val name: String,
    public val columns: Map<String, Column>,
    public val constraints: Map<String, Constraint>,
    public val ifNotExists: Boolean = false,
    public val compositePrimaryKey: Set<String>? = null,
) {
    public data class Column(
        public val type: DialectDbType,
        public val nullable: Boolean = false,
        public val primary: Boolean = false,
        public val unique: String? = null,
        public val foreign: ForeignColumnConfig? = null
    ) {
        public data class ForeignColumnConfig(
            val constraintName: String,
            val foreignTable: String,
            val foreignColumn: String
        )
    }

    public sealed interface Constraint {
        public class UniqueConstraint(public val columns: List<String>) : Constraint
        public class ForeignKey(public val foreignTable: String, public val columnMapping: Map<String, String>) :
            Constraint
    }
}
