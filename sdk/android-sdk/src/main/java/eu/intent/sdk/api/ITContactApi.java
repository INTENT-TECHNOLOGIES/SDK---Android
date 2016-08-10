package eu.intent.sdk.api;

import android.content.Context;

import java.util.List;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITContact;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * A wrapper to call the Contact API.
 */
public class ITContactApi {
    private Service mService;

    public ITContactApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Adds a contact only visible to the authenticated user.
     *
     * @param name   the contact name
     * @param number the contact number
     */
    public void add(String name, String number, ITApiCallback<Void> callback) {
        ITContact contact = new ITContact();
        contact.name = name;
        contact.number = number;
        mService.add(contact).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Removes a contact previously created by the authenticated user.
     *
     * @param name   the contact name
     * @param number the contact number
     */
    public void delete(String name, String number, ITApiCallback<Void> callback) {
        ITContact contact = new ITContact();
        contact.name = name;
        contact.number = number;
        mService.delete(contact).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets all the contacts visible to the authenticated user.
     */
    public void get(ITApiCallback<List<ITContact>> callback) {
        mService.get().enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("residentservices/v1/contacts")
        Call<List<ITContact>> get();

        @POST("residentservices/v1/contacts/add")
        Call<Void> add(@Body ITContact contact);

        @POST("residentservices/v1/contacts/remove")
        Call<Void> delete(@Body ITContact contact);
    }
}
