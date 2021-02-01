package factory.mikrate.dialects.api

public open class NotAvailableError(message: String, cause: Throwable? = null) : Exception(message, cause)
