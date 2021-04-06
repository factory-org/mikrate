package factory.mikrate.dialects.api

public interface CoreDialect {
    /**
     * This id must be unique for each dialect
     */
    public val id: String
    public val types: TypeSqlGen
    public val constraints: ConstraintSqlGen
    public val creation: CreationSqlGen
    public val alter: AlterSqlGen
}
