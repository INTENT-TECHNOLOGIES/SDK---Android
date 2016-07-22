package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.util.List;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * An occupant has access to contact numbers, either created by the lessor, or created by the user himself.
 */
public class ITContact implements Parcelable {
    public static final Parcelable.Creator<ITContact> CREATOR = new Parcelable.Creator<ITContact>() {
        public ITContact createFromParcel(Parcel source) {
            return new ITContact(source);
        }

        public ITContact[] newArray(int size) {
            return new ITContact[size];
        }
    };
    private static Service sService;
    public String name;
    public String number;

    transient public Visibility visibility;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITContact() {
        // Needed by Retrofit
    }

    private ITContact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    protected ITContact(Parcel in) {
        name = in.readString();
        number = in.readString();
        int tmpVisibility = in.readInt();
        visibility = tmpVisibility == -1 ? null : Visibility.values()[tmpVisibility];
        custom = in.readBundle();
    }

    /**
     * Adds a contact only visible to the authenticated user.
     *
     * @param name   the contact name
     * @param number the contact number
     */
    public static void add(Context context, String name, String number, ITApiCallback<Void> callback) {
        getServiceInstance(context).add(new ITContact(name, number)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Removes a contact previously created by the authenticated user.
     *
     * @param name   the contact name
     * @param number the contact number
     */
    public static void delete(Context context, String name, String number, ITApiCallback<Void> callback) {
        getServiceInstance(context).delete(new ITContact(name, number)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets all the contacts visible to the authenticated user.
     */
    public static void get(Context context, ITApiCallback<List<ITContact>> callback) {
        getServiceInstance(context).get().enqueue(new ProxyCallback<>(callback));
    }

    private static Service getServiceInstance(Context context) {
        synchronized (ITContact.class) {
            if (sService == null) {
                sService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
            }
        }
        return sService;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
        dest.writeInt(visibility == null ? -1 : visibility.ordinal());
        dest.writeBundle(custom);
    }

    public enum Visibility {
        /**
         * This contact is only visible to the user who created it
         */
        PRIVATE,
        /**
         * This contact is visible by all the users of the same domain
         */
        PUBLIC
    }

    private interface Service {
        @GET("residentservices/v1/contacts")
        Call<List<ITContact>> get();

        @POST("residentservices/v1/contacts/add")
        Call<Void> add(@Body ITContact contact);

        @POST("residentservices/v1/contacts/remove")
        Call<Void> delete(@Body ITContact contact);
    }

    public static class Deserializer implements JsonDeserializer<ITContact> {
        @Override
        public ITContact deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ITContact contact = new Gson().fromJson(json, typeOfT);
            contact.visibility = json.getAsJsonObject().get("isPrivate").getAsBoolean() ? Visibility.PRIVATE : Visibility.PUBLIC;
            return contact;
        }
    }
}
