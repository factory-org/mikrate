package factory.mikrate.dsl

import io.kotest.core.config.AbstractProjectConfig

object ProjectConfig : AbstractProjectConfig() {
    override val parallelism = Runtime.getRuntime().availableProcessors()
    override val globalAssertSoftly = true
}
