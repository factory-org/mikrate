package factory.mikrate.dialects.h2

import io.kotest.core.config.AbstractProjectConfig

@Suppress("unused")
object ProjectConfig : AbstractProjectConfig() {
    override val parallelism = Runtime.getRuntime().availableProcessors()
}
