package eu.intent.sdk;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITApiError;
import eu.intent.sdk.auth.ITClient;
import eu.intent.sdk.auth.ITOAuthApi;
import eu.intent.sdk.auth.ITOAuthManager;
import eu.intent.sdk.auth.ITOAuthToken;
import eu.intent.sdk.auth.ITSessionManager;
import eu.intent.sdk.util.ITEnvironment;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@SuppressWarnings("PMD")
public class ITOAuthManagerTest {
    private final ITEnvironment mEnvironment = new ITEnvironment("https://fake.com/auth/", "https://fake.com/api/", "https://fake.com/redirect/");
    private final ITClient mClient = new ITClient(mEnvironment, "clientId", "clientSecret", "redirectUri");
    private final ITOAuthToken mAccessToken = new ITOAuthToken(100000, "access", "refresh", "bearer");
    private final ITOAuthToken mExpiredAccessToken = new ITOAuthToken(-100000, "access", "refresh", "bearer");
    private final ITOAuthToken mExpiredAccessTokenNoRefresh = new ITOAuthToken(-100000, "access", "", "bearer");
    private final ITOAuthToken mNewAccessToken = new ITOAuthToken(100000, "new_access", "new_refresh", "bearer");
    private final ITSessionManager mSessionManager = new ITSessionManager(RuntimeEnvironment.application);
    private Call<ITOAuthToken> mCall;
    private final ITOAuthManager mOauthManager = new ITOAuthManager(RuntimeEnvironment.application, mClient, new ITOAuthApi() {
        @NotNull
        @Override
        public Call<ITOAuthToken> refreshToken(@NotNull String grantType, @NotNull String refreshToken, @NotNull String clientId, @NotNull String clientSecret) {
            return mCall;
        }

        @NotNull
        @Override
        public Call<ITOAuthToken> requestTokenFromCode(@NotNull String grantType, @NotNull String code, @NotNull String clientId, @NotNull String clientSecret, @NotNull String redirectUri) {
            return mCall;
        }

        @NotNull
        @Override
        public Call<ITOAuthToken> requestTokenFromCredentials(@NotNull String grantType, @NotNull String username, @NotNull String password, @NotNull String clientId, @NotNull String clientSecret) {
            return mCall;
        }
    });

    @After
    public void cleanup() {
        mSessionManager.logout();
        mCall = null;
    }

    @Test
    public void noSession() {
        assertFalse(mSessionManager.isLoggedIn());
    }

    @Test
    public void loginFromCodeSuccess() {
        mCall = new MockCall<>(true, mAccessToken);
        mOauthManager.requestToken("code", new ITApiCallback<ITOAuthToken>() {
            @Override
            public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                fail("The request should not fail");
            }

            @Override
            public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                assertTrue("There should be a session", mSessionManager.isLoggedIn());
            }
        });
    }

    @Test
    public void loginFromCodeFailure() {
        mCall = new MockCall<>(false);
        mOauthManager.requestToken("code", new ITApiCallback<ITOAuthToken>() {
            @Override
            public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                assertFalse("There should be no session", mSessionManager.isLoggedIn());
            }

            @Override
            public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                fail("The request should fail");
            }
        });
    }

    @Test
    public void loginFromCredentialsSuccess() {
        mCall = new MockCall<>(true, mAccessToken);
        mOauthManager.requestToken("username", "password", new ITApiCallback<ITOAuthToken>() {
            @Override
            public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                fail("The request should not fail");
            }

            @Override
            public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                assertTrue("There should be a session", mSessionManager.isLoggedIn());
            }
        });
    }

    @Test
    public void loginFromCredentialsFailure() {
        mCall = new MockCall<>(false);
        mOauthManager.requestToken("username", "password", new ITApiCallback<ITOAuthToken>() {
            @Override
            public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                assertFalse("There should be no session", mSessionManager.isLoggedIn());
            }

            @Override
            public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                fail("The request should fail");
            }
        });
    }

    @Test
    public void refreshTokenAsyncWithRefreshToken() {
        mCall = new MockCall<>(true, mExpiredAccessToken);
        mOauthManager.requestToken("code", new ITApiCallback<ITOAuthToken>() {
            @Override
            public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                fail("The request should not fail");
            }

            @Override
            public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                assertFalse("The session should be expired", mSessionManager.isLoggedIn());
                assertTrue("There should be a refresh token", mSessionManager.canRefreshToken());
                mCall = new MockCall<>(true, mNewAccessToken);
                mOauthManager.refreshToken(new ITApiCallback<ITOAuthToken>() {
                    @Override
                    public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                        fail("The request should not fail");
                    }

                    @Override
                    public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                        assertTrue("There should be a new session", mSessionManager.isLoggedIn());
                        assertEquals("The access token should be updated", "new_access", mSessionManager.getAccessToken());
                        assertEquals("The refresh token should be updated", "new_refresh", mSessionManager.getRefreshToken());
                    }
                });
            }
        });
    }

    @Test
    public void refreshTokenSyncWithRefreshToken() {
        mCall = new MockCall<>(true, mExpiredAccessToken);
        mOauthManager.requestToken("code", new ITApiCallback<ITOAuthToken>() {
            @Override
            public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                fail("The request should not fail");
            }

            @Override
            public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                assertFalse("The session should be expired", mSessionManager.isLoggedIn());
                assertTrue("There should be a refresh token", mSessionManager.canRefreshToken());
                mCall = new MockCall<>(true, mNewAccessToken);
                ITOAuthToken newToken = mOauthManager.refreshToken();
                assertEquals("The returned token should be the new token", mNewAccessToken, newToken);
                assertTrue("There should be a new session", mSessionManager.isLoggedIn());
                assertEquals("The access token should be refreshed", "new_access", mSessionManager.getAccessToken());
                assertEquals("The refresh token should be refreshed", "new_refresh", mSessionManager.getRefreshToken());
            }
        });
    }

    @Test
    public void refreshTokenAsyncWithNoRefreshToken() {
        mCall = new MockCall<>(true, mExpiredAccessTokenNoRefresh);
        mOauthManager.requestToken("code", new ITApiCallback<ITOAuthToken>() {
            @Override
            public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                fail("The request should not fail");
            }

            @Override
            public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                assertFalse("The session should be expired", mSessionManager.isLoggedIn());
                assertFalse("There should not be a refresh token", mSessionManager.canRefreshToken());
                mCall = new MockCall<>(false);
                mOauthManager.refreshToken(new ITApiCallback<ITOAuthToken>() {
                    @Override
                    public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                        assertFalse("There should not be a new session", mSessionManager.isLoggedIn());
                        assertEquals("The access token should not be updated", "access", mSessionManager.getAccessToken());
                        assertFalse("The refresh token should not be updated", mSessionManager.canRefreshToken());
                    }

                    @Override
                    public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                        fail("The request should fail");
                    }
                });
            }
        });
    }

    @Test
    public void refreshTokenSyncWithNoRefreshToken() {
        mCall = new MockCall<>(true, mExpiredAccessTokenNoRefresh);
        mOauthManager.requestToken("code", new ITApiCallback<ITOAuthToken>() {
            @Override
            public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                fail("The request should not fail");
            }

            @Override
            public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                assertFalse("The session should be expired", mSessionManager.isLoggedIn());
                assertFalse("There should be no refresh token", mSessionManager.canRefreshToken());
                mCall = new MockCall<>(false);
                ITOAuthToken newToken = mOauthManager.refreshToken();
                assertNull("The returned token should be null", newToken);
                assertFalse("There should not be a new session", mSessionManager.isLoggedIn());
                assertEquals("The access token should not be refreshed", "access", mSessionManager.getAccessToken());
                assertFalse("The refresh token should not be refreshed", mSessionManager.canRefreshToken());
            }
        });
    }

    private static final class MockCall<T> implements Call<T> {
        private boolean mSuccess;
        @Nullable
        private T mResult;

        MockCall(boolean success, @Nullable T result) {
            mSuccess = success;
            mResult = result;
        }

        MockCall(boolean success) {
            this(success, null);
        }

        @Override
        public Response<T> execute() throws IOException {
            if (mSuccess) {
                return Response.success(mResult);
            } else {
                return Response.error(403, ResponseBody.create(MediaType.parse("application/json"), "{}"));
            }
        }

        @Override
        public void enqueue(Callback<T> callback) {
            if (mSuccess) {
                callback.onResponse(this, Response.success(mResult));
            } else {
                callback.onFailure(this, new Throwable("Fake error"));
            }
        }

        @Override
        public boolean isExecuted() {
            return false;
        }

        @Override
        public void cancel() {

        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Override
        public Call<T> clone() {
            return null;
        }

        @Override
        public Request request() {
            return null;
        }
    }
}
