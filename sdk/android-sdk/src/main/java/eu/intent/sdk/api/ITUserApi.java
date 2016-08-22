package eu.intent.sdk.api;

import android.content.Context;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

/**
 * A wrapper to call the User API.
 */
public class ITUserApi {
    private Service mService;

    public ITUserApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Gets the current authenticated user.
     */
    public void getCurrentUser(ITApiCallback<ITUser> callback) {
        mService.getUser().enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Updates the current authenticated user with the data of the given ITUser.
     */
    public void updateCurrentUser(ITUser user, ITApiCallback<ITUser> callback) {
        mService.updateUser(new Service.UserUpdate(user)).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("accounts/v1/me")
        Call<ITUser> getUser();

        @PUT("accounts/v1/me")
        Call<ITUser> updateUser(@Body UserUpdate body);

        class UserUpdate {
            public String firstname;
            public String lastname;
            public String mobile;
            public String phone;

            public UserUpdate(ITUser user) {
                firstname = user.firstName;
                lastname = user.lastName;
                mobile = user.mobile;
                phone = user.phone;
            }
        }
    }
}
