module mikrate.dsl {
    requires transitive kotlin.stdlib;
    requires transitive mikrate.core;

    exports factory.mikrate.dsl;
    exports factory.mikrate.dsl.builders;
}
