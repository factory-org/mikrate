module mikrate.dsl {
    requires transitive kotlin.stdlib;
    requires transitive mikrate.core;
    requires mikrate.dialects.api;

    exports factory.mikrate.dsl;
    exports factory.mikrate.dsl.builders;
    exports factory.mikrate.dsl.contexts;
    exports factory.mikrate.dsl.helpers;
}
