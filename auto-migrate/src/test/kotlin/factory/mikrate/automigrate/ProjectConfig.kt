package factory.mikrate.automigrate

import io.kotest.core.config.AbstractProjectConfig

@Suppress("unused")
object ProjectConfig : AbstractProjectConfig() {
    override val parallelism = Runtime.getRuntime().availableProcessors()
}
