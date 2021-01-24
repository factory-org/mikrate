module mikrate.executors.jdbc {
    requires transitive kotlin.stdlib;
    requires transitive mikrate.executors.api;
    requires java.sql;
    requires kotlin.stdlib.jdk7;

    exports factory.mikrate.executors.jdbc;
}
