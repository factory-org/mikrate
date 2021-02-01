module mikrate.core {
    requires transitive kotlin.stdlib;
    requires mikrate.dialects.api;

    exports factory.mikrate.core;
    exports factory.mikrate.core.actions;
    exports factory.mikrate.core.types;
}
