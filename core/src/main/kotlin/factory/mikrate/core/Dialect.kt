package factory.mikrate.core

public enum class Dialect {
    Sqlite {
        override fun toString(): String = "SQLite"
    },
    Postgres {
        override fun toString(): String = "PostgreSQL"
    }
}
