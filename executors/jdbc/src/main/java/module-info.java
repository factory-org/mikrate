module mikrate.executors.jdbc {
    requires transitive mikrate.executors.api;
    requires java.sql;
    requires kotlin.stdlib;
    requires mikrate.dialects.api;

    exports factory.mikrate.executors.jdbc;
}
