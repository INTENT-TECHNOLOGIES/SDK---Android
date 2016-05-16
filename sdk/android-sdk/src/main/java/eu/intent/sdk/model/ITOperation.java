package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * An operation is a set of tasks, assigned to a contractor by a client.
 */
public class ITOperation implements Parcelable {
    public static final Parcelable.Creator<ITOperation> CREATOR = new Parcelable.Creator<ITOperation>() {
        public ITOperation createFromParcel(Parcel source) {
            return new ITOperation(source);
        }

        public ITOperation[] newArray(int size) {
            return new ITOperation[size];
        }
    };

    private static Service sService;

    public boolean active;
    @SerializedName("entity")
    public String client;
    @SerializedName("installer")
    public String contractor;
    @SerializedName("_id")
    public String id;
    @SerializedName("updated")
    public long lastUpdate;
    @SerializedName("label")
    public String name;
    public boolean published;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITOperation() {
    }

    protected ITOperation(Parcel in) {
        active = in.readByte() != 0;
        client = in.readString();
        contractor = in.readString();
        id = in.readString();
        lastUpdate = in.readLong();
        name = in.readString();
        published = in.readByte() != 0;
        custom = in.readBundle();
    }

    /**
     * Gets a list of all the operations.
     */
    public static void get(Context context, ITApiCallback<ITOperationList> callback) {
        getServiceInstance(context).get().enqueue(new ProxyCallback<>(callback));
    }

    private static Service getServiceInstance(Context context) {
        if (sService == null) {
            sService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
        }
        return sService;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(active ? (byte) 1 : (byte) 0);
        dest.writeString(client);
        dest.writeString(contractor);
        dest.writeString(id);
        dest.writeLong(lastUpdate);
        dest.writeString(name);
        dest.writeByte(published ? (byte) 1 : (byte) 0);
        dest.writeBundle(custom);
    }

    private interface Service {
        @GET("v1/operations?beginIndex=0&number=9999")
        Call<ITOperationList> get();
    }
}
