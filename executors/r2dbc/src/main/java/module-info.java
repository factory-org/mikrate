module mikrate.executors.r2dbc {
    requires transitive mikrate.executors.api;
    requires r2dbc.spi;
    //requires kotlinx.coroutines.reactive; // TODO: Comment in once https://github.com/Kotlin/kotlinx.coroutines/issues/2237 is fixed
}
