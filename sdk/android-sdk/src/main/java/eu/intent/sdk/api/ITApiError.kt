package eu.intent.sdk.api

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Response


/**
 * An error as returned by the Intent API.
 */
class ITApiError(
        val httpCode: Int = -1,
        val httpMessage: String = "",
        val code: String = "",
        val details: String = "",
        val message: String = ""
) {
    internal companion object Helper {
        internal val PARSER: JsonParser = JsonParser()

        internal fun <T> parseError(response: Response<T>): ITApiError {
            val httpCode: Int = response.code()
            val httpMessage: String = response.message() ?: ""
            var code: String = ""
            var details: String = ""
            var message: String = ""
            val json: JsonElement = PARSER.parse(response.errorBody().string())
            if (json.isJsonObject) {
                val jsonObject: JsonObject = json.asJsonObject
                code = jsonObject["code"]?.asString ?: ""
                details = jsonObject["details"]?.asString ?: ""
                if (jsonObject.has("message")) {
                    message = jsonObject["message"].asString ?: ""
                } else if (jsonObject.has("error_description")) {
                    message = jsonObject["error_description"].asString ?: ""
                }
            }
            return ITApiError(httpCode, httpMessage, code, details, message)
        }
    }
}