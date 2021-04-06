package factory.mikrate.dialects.api

/**
 * Indicates if the support status of a feature.
 */
public sealed class SupportStatus {
    /**
     * The feature is supported.
     */
    public object Supported : SupportStatus()

    /**
     * It isn't known whether the feature is supported with the specific configuration, backend version or similar.
     *
     * @param reason May be given to allow the developer to resolve issues
     */
    public data class Unknown(val reason: String? = null) : SupportStatus()

    /**
     * Indicates that a feature is unsupported.
     *
     * @param reason May be given to allow the developer to resolve issues
     */
    public data class Unsupported(val reason: String? = null) : SupportStatus()
}
