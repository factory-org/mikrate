package factory.mikrate.dialects.api.models

public sealed interface DialectDbType {
    public object BooleanType : DialectDbType
    public object ByteType : DialectDbType
    public class IntegerType(public val size: Short = 4) : DialectDbType
    public object TextType : DialectDbType
    public object UuidType : DialectDbType
    public class VarcharType(public val length: Int) : DialectDbType
    public class EnumType(public val name: String) : DialectDbType
    public interface Other : DialectDbType
}
