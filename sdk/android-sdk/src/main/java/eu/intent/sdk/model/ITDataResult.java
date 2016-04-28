package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of ITData.
 */
public class ITDataResult implements Parcelable {
    public static final Parcelable.Creator<ITDataResult> CREATOR = new Parcelable.Creator<ITDataResult>() {
        public ITDataResult createFromParcel(Parcel source) {
            return new ITDataResult(source);
        }

        public ITDataResult[] newArray(int size) {
            return new ITDataResult[size];
        }
    };

    public List<ITData> data = new ArrayList<>();
    public ITStream stream;
    @SerializedName("total")
    public int totalCount = 0;
    public ITData.Type type = ITData.Type.UNKNOWN;

    transient public String fromDomain;
    transient public String issuerDomain;
    transient public String sponsor;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITDataResult() {
    }

    protected ITDataResult(Parcel in) {
        data = in.createTypedArrayList(ITData.CREATOR);
        fromDomain = in.readString();
        issuerDomain = in.readString();
        sponsor = in.readString();
        stream = in.readParcelable(ITStream.class.getClassLoader());
        totalCount = in.readInt();
        int tmpType = in.readInt();
        type = tmpType == -1 ? null : ITData.Type.values()[tmpType];
        custom = in.readBundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
        dest.writeString(fromDomain);
        dest.writeString(issuerDomain);
        dest.writeString(sponsor);
        dest.writeParcelable(stream, flags);
        dest.writeInt(totalCount);
        dest.writeInt(type == null ? -1 : type.ordinal());
        dest.writeBundle(custom);
    }

    public static class Deserializer implements JsonDeserializer<ITDataResult> {
        @Override
        public ITDataResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ITDataResult result = new Gson().fromJson(json, typeOfT);
            JsonObject headers = json.getAsJsonObject().getAsJsonObject("headers");
            result.fromDomain = headers.get("fromDomain").getAsString();
            result.issuerDomain = headers.get("issuerDomain").getAsString();
            result.sponsor = headers.get("sponsor").getAsString();
            return result;
        }
    }
}
