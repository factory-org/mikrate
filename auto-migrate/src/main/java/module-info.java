module mikrate.automigrate {
    requires transitive kotlin.stdlib;
    requires mikrate.core;
    requires mikrate.executors.api;
    requires mikrate.dialects.api;

    exports factory.mikrate.automigrate;
}
