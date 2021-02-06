package factory.mikrate.dsl.helpers

/**
 * Represents a reference to a column.
 */
public class ColumnRef internal constructor(
    /**
     * @suppress
     */
    public val table: String,
    /**
     * @suppress
     */
    public val column: String
)
