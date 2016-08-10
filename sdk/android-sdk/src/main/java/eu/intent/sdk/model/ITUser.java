package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * A user can be either a user of an application, or an occupant of a part.
 */
public class ITUser implements Parcelable {
    public static final Parcelable.Creator<ITUser> CREATOR = new Parcelable.Creator<ITUser>() {
        public ITUser createFromParcel(Parcel source) {
            return new ITUser(source);
        }

        public ITUser[] newArray(int size) {
            return new ITUser[size];
        }
    };

    public String domain;
    public String email;
    @SerializedName("firstname")
    public String firstName;
    public String id;
    @SerializedName("lastname")
    public String lastName;
    public String mobile;
    public String phone;
    public String username;

    transient public List<String> entityRoles = new ArrayList<>();
    transient public List<String> userRoles = new ArrayList<>();

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITUser() {
    }

    protected ITUser(Parcel in) {
        domain = in.readString();
        email = in.readString();
        firstName = in.readString();
        id = in.readString();
        lastName = in.readString();
        mobile = in.readString();
        phone = in.readString();
        username = in.readString();
        entityRoles = in.createStringArrayList();
        userRoles = in.createStringArrayList();
        custom = in.readBundle();
    }

    /**
     * Returns a concatenation of the user's first name and last name. If these are empty, return the username.
     */
    public String getFullName() {
        ArrayList<String> names = new ArrayList<>(Arrays.asList(new String[]{firstName, lastName}));
        names.removeAll(Collections.singleton((String) null));
        String fullName = TextUtils.join(" ", names).trim();
        return !TextUtils.isEmpty(fullName) ? fullName : username;
    }

    /**
     * Returns one phone number among the user's phone number.
     */
    public String getOnePhone() {
        if (!TextUtils.isEmpty(mobile)) {
            return mobile;
        } else {
            return phone;
        }
    }

    /**
     * Returns a concatenation of the user's first name and the first letter of his last name. If these are empty, return the username.
     */
    public String getShortName() {
        String firstLetterOfLastName = !TextUtils.isEmpty(lastName) ? lastName.toUpperCase(Locale.US).charAt(0) + "." : "";
        ArrayList<String> names = new ArrayList<>(Arrays.asList(new String[]{firstName, firstLetterOfLastName}));
        names.removeAll(Collections.singleton((String) null));
        String shortName = TextUtils.join(" ", names).trim();
        return !TextUtils.isEmpty(shortName) ? shortName : username;
    }

    /**
     * Returns true if this user is a "home" account, false otherwise
     */
    public boolean isHomeAccount() {
        return domain != null && domain.contains(".homes.");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(domain);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(id);
        dest.writeString(lastName);
        dest.writeString(mobile);
        dest.writeString(phone);
        dest.writeString(username);
        dest.writeStringList(entityRoles);
        dest.writeStringList(userRoles);
        dest.writeBundle(custom);
    }

    public static class Deserializer implements JsonDeserializer<ITUser> {
        @Override
        public ITUser deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new Gson();
            ITUser user = gson.fromJson(json, typeOfT);
            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.has("entityRoles") && jsonObject.get("entityRoles").isJsonArray()) {
                JsonArray entityRoles = jsonObject.get("entityRoles").getAsJsonArray();
                for (JsonElement entityRole : entityRoles) {
                    JsonObject role = entityRole.getAsJsonObject();
                    user.entityRoles.add(role.get("name").getAsString());
                }
            }
            if (jsonObject.has("userRoles") && jsonObject.get("userRoles").isJsonArray()) {
                JsonArray userRoles = jsonObject.get("userRoles").getAsJsonArray();
                for (JsonElement userRole : userRoles) {
                    JsonObject role = userRole.getAsJsonObject();
                    user.userRoles.add(role.get("name").getAsString());
                }
            }
            return user;
        }
    }
}
