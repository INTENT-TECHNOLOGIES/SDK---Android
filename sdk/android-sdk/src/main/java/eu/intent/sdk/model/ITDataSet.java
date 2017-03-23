package eu.intent.sdk.model;

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
public class ITDataSet implements Parcelable {
    public static final Parcelable.Creator<ITDataSet> CREATOR = new Parcelable.Creator<ITDataSet>() {
        public ITDataSet createFromParcel(Parcel source) {
            return new ITDataSet(source);
        }

        public ITDataSet[] newArray(int size) {
            return new ITDataSet[size];
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

    public ITDataSet() {
        // Needed by Gson
    }

    protected ITDataSet(Parcel in) {
        data = in.createTypedArrayList(ITData.CREATOR);
        fromDomain = in.readString();
        issuerDomain = in.readString();
        sponsor = in.readString();
        stream = in.readParcelable(ITStream.class.getClassLoader());
        totalCount = in.readInt();
        int tmpType = in.readInt();
        type = tmpType == -1 ? null : ITData.Type.values()[tmpType];
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
    }

    public static class Deserializer implements JsonDeserializer<ITDataSet> {
        @Override
        public ITDataSet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ITDataSet result = new Gson().fromJson(json, typeOfT);
            JsonObject headers = json.getAsJsonObject().getAsJsonObject("headers");
            result.fromDomain = headers.get("fromDomain").getAsString();
            result.issuerDomain = headers.get("issuerDomain").getAsString();
            result.sponsor = headers.get("sponsor").getAsString();
            if (result.type == null) {
                result.type = ITData.Type.UNKNOWN;
            }
            return result;
        }
    }
}
