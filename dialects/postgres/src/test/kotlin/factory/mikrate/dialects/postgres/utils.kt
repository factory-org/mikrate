package factory.mikrate.dialects.postgres

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.lifecycle.Startable
import java.sql.Connection
import java.sql.DriverManager
import java.time.Duration

fun testContainer(image: String): GenericContainer<*> {
    return GenericContainer<Nothing>(image)
}

fun createPostgresContainer(): PostgresContainer = PostgresContainer(
    testContainer("postgres:16-alpine")
        .withEnv("POSTGRES_PASSWORD", "postgres")
        .withExposedPorts(5432)
        .waitingFor(HostPortWaitStrategy())
        .waitingFor(
            LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*").withTimes(2)
        )
        .withStartupTimeout(Duration.ofSeconds(20))
)

class PostgresContainer(private val container: GenericContainer<*>) : Startable by container {
    fun connect(): Connection {
        return DriverManager.getConnection(
            "jdbc:postgresql://${container.host}:${container.firstMappedPort}/",
            "postgres",
            "postgres"
        )
    }
}
