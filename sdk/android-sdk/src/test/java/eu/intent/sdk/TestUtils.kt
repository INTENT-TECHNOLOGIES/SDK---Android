package eu.intent.sdk

import com.google.gson.Gson
import eu.intent.sdk.network.ITRetrofitHelper

object TestUtils {
    val gson: Gson by lazy { ITRetrofitHelper.getDefaultGsonBuilder().create() }

    fun getResourceAsString(filename: String): String {
        return javaClass.classLoader.getResourceAsStream(filename).bufferedReader().use { it.readText() }
    }
}