package factory.mikrate.dsl.helpers

/**
 * Represents a reference to a table
 */
public class TableRef internal constructor(
    /**
     * @suppress
     */
    public val tableName: String
) {
    public fun column(name: String): ColumnRef = ColumnRef(tableName, name)
}
