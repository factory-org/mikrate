module mikrate.core {
    requires transitive kotlin.stdlib;
    requires mikrate.dialects.api;
    requires org.jetbrains.annotations;

    exports factory.mikrate.core;
    exports factory.mikrate.core.actions;
    exports factory.mikrate.core.types;
}
