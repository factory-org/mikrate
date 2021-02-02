package factory.mikrate.automigrate

import java.security.MessageDigest

internal fun hashMigrationId(id: String): ByteArray {
    val digest = MessageDigest.getInstance("SHA3-256")
    return digest.digest(id.toByteArray(Charsets.UTF_8))
}
