module mikrate.executors.r2dbc {
    requires transitive mikrate.executors.api;
    requires r2dbc.spi;
    requires kotlinx.coroutines.reactive;
}
