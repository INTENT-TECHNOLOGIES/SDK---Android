package eu.intent.sdk.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import eu.intent.sdk.ui.activity.ITAuthActivity

/**
 * An helper to login to the Intent platform.
 */
object ITAuthHelper {
    /**
     * Creates a login Intent to be started from your code.
     * @param client the client containing your API credentials
     */
    fun createLoginActivityIntent(context: Context, client: ITClient): Intent {
        val intent = Intent(context, ITAuthActivity::class.java)
        intent.putExtra(ITAuthActivity.EXTRA_CLIENT, client)
        return intent
    }

    /**
     * Starts an authentication activity with the given authentication request.
     * @param activity    the activity you want to open the login activity from
     * @param client     the session containing your API credentials
     * @param requestCode the activity will be opened with startActivityForResult, therefore you'll be able to catch the result in onActivityResult with the given request code
     */
    fun openLoginActivity(activity: Activity, client: ITClient, requestCode: Int) {
        val intent = createLoginActivityIntent(activity, client)
        activity.startActivityForResult(intent, requestCode)
    }
}