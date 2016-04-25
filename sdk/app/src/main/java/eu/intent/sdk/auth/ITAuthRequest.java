package eu.intent.sdk.auth;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.intent.sdk.auth.internal.Oauth;
import okhttp3.HttpUrl;

/**
 * When creating an ITAuthActivity you must pass parameters through this class.
 */
public final class ITAuthRequest {
    public HttpUrl url;

    public static class Builder {
        private static final String PATH_OAUTH = "oauth";
        private static final String PATH_AUTHORIZE = "authorize";
        private static final String PARAM_CLIENT_ID = "client_id";
        private static final String PARAM_REDIRECT_URI = "redirect_uri";
        private static final String PARAM_RESPONSE_TYPE = "response_type";
        private static final String PARAM_RESPONSE_TYPE_CODE = "code";
        private static final String PARAM_SCOPES = "scope";

        private Oauth mOauth;

        private List<String> mScopes = new ArrayList<>();

        public Builder(Context context) {
            mOauth = Oauth.getInstance(context);
        }

        /**
         * Appends the given addScope to the list of the addScopes you're asking authorization for.
         */
        public Builder addScope(String scope) {
            mScopes.add(scope);
            return this;
        }

        /**
         * Appends the given addScopes to the list of the addScopes you're asking authorization for.
         */
        public Builder addScopes(String... scopes) {
            mScopes.addAll(Arrays.asList(scopes));
            return this;
        }

        public ITAuthRequest build() {
            ITAuthRequest request = new ITAuthRequest();
            request.url = HttpUrl.parse(mOauth.getBaseUrl()).newBuilder()
                    .addPathSegment(PATH_OAUTH).addPathSegment(PATH_AUTHORIZE)
                    .addQueryParameter(PARAM_RESPONSE_TYPE, PARAM_RESPONSE_TYPE_CODE)
                    .addQueryParameter(PARAM_SCOPES, TextUtils.join(" ", mScopes))
                    .addQueryParameter(PARAM_CLIENT_ID, mOauth.getClientId())
                    .addEncodedQueryParameter(PARAM_REDIRECT_URI, mOauth.getRedirectUrl())
                    .build();
            return request;
        }
    }
}
