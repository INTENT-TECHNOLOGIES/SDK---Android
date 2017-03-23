package eu.intent.sdk.auth

internal enum class ITOAuthGrantType(val type: String) {
    CODE("authorization_code"),
    PASSWORD("password"),
    REFRESH("refresh_token")
}