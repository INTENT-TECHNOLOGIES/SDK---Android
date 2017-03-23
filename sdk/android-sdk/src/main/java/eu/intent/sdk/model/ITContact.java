package eu.intent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

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
    public String name;
    public String number;

    transient public Visibility visibility;

    public ITContact() {
        // Needed by Retrofit
    }

    protected ITContact(Parcel in) {
        name = in.readString();
        number = in.readString();
        int tmpVisibility = in.readInt();
        visibility = tmpVisibility == -1 ? null : Visibility.values()[tmpVisibility];
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

    public static class Deserializer implements JsonDeserializer<ITContact> {
        @Override
        public ITContact deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ITContact contact = new Gson().fromJson(json, typeOfT);
            contact.visibility = json.getAsJsonObject().get("isPrivate").getAsBoolean() ? Visibility.PRIVATE : Visibility.PUBLIC;
            return contact;
        }
    }
}
