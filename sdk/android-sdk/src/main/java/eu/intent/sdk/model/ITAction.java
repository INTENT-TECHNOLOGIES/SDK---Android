package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An action is a step of an ITTask. An action has a template, which defines its payload.
 *
 * @see ITTask
 */
public class ITAction implements Parcelable {
    public static final Parcelable.Creator<ITAction> CREATOR = new Parcelable.Creator<ITAction>() {
        public ITAction createFromParcel(Parcel source) {
            return new ITAction(source);
        }

        public ITAction[] newArray(int size) {
            return new ITAction[size];
        }
    };

    @SerializedName("auto")
    public boolean automatic;
    public String comment;
    public boolean error;
    public boolean finished;
    @SerializedName("updated")
    public long lastUpdate;
    public Payload payload = new Payload();
    @SerializedName("actionTemplateId")
    public String templateId;

    transient public int index;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! It is not saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITAction() {
        // Needed by Retrofit
    }

    protected ITAction(Parcel in) {
        automatic = in.readByte() != 0;
        comment = in.readString();
        error = in.readByte() != 0;
        finished = in.readByte() != 0;
        index = in.readInt();
        lastUpdate = in.readLong();
        payload = in.readParcelable(Payload.class.getClassLoader());
        templateId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(automatic ? (byte) 1 : (byte) 0);
        dest.writeString(comment);
        dest.writeByte(error ? (byte) 1 : (byte) 0);
        dest.writeByte(finished ? (byte) 1 : (byte) 0);
        dest.writeInt(index);
        dest.writeLong(lastUpdate);
        dest.writeParcelable(payload, flags);
        dest.writeString(templateId);
    }

    /**
     * An action can provide additional data. Not all the properties of the Payload object may be set, depending on the action template.
     */
    public static class Payload implements Parcelable {
        public static final Creator<Payload> CREATOR = new Creator<Payload>() {
            public Payload createFromParcel(Parcel source) {
                return new Payload(source);
            }

            public Payload[] newArray(int size) {
                return new Payload[size];
            }
        };

        public String assetId;
        @SerializedName("connection_point")
        public String connectionPoint;
        public String deviceId;
        @SerializedName("level")
        public String floor;
        @SerializedName("room")
        public String installRoom;
        public List<Repeater> repeaters = new ArrayList<>();

        /**
         * Device output -> activity
         */
        public transient Map<String, String> deviceBindings = new ConcurrentHashMap<>();
        /**
         * Device output -> settings (key/value)
         */
        public transient Map<String, Settings> settings = new ConcurrentHashMap<>();
        /**
         * Device output -> room
         */
        public transient Map<String, String> usageRooms;

        public Payload() {
            // Needed by Retrofit
        }

        protected Payload(Parcel in) {
            assetId = in.readString();
            connectionPoint = in.readString();
            deviceId = in.readString();
            floor = in.readString();
            installRoom = in.readString();
            Bundle bindingsBundle = in.readBundle();
            for (String output : bindingsBundle.keySet()) {
                deviceBindings.put(output, bindingsBundle.getString(output));
            }
            repeaters = in.createTypedArrayList(Repeater.CREATOR);
            int settingsSize = in.readInt();
            for (int i = 0; i < settingsSize; i++) {
                String output = in.readString();
                Settings settings = in.readParcelable(Settings.class.getClassLoader());
                this.settings.put(output, settings);
            }
            boolean usageRoomsNotNull = in.readByte() > 0;
            Bundle roomsBundle = in.readBundle();
            if (usageRoomsNotNull) {
                usageRooms = new HashMap<>();
                for (String output : roomsBundle.keySet()) {
                    usageRooms.put(output, roomsBundle.getString(output));
                }
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(assetId);
            dest.writeString(connectionPoint);
            dest.writeString(deviceId);
            dest.writeString(floor);
            dest.writeString(installRoom);
            Bundle bindingsBundle = new Bundle();
            for (Map.Entry<String, String> binding : deviceBindings.entrySet()) {
                bindingsBundle.putString(binding.getKey(), binding.getValue());
            }
            dest.writeBundle(bindingsBundle);
            dest.writeTypedList(repeaters);
            dest.writeInt(settings.size());
            for (Map.Entry<String, Settings> setting : settings.entrySet()) {
                dest.writeString(setting.getKey());
                dest.writeParcelable(setting.getValue(), flags);
            }
            dest.writeByte((byte) (usageRooms != null ? 1 : 0));
            Bundle roomsBundle = new Bundle();
            if (usageRooms != null) {
                for (Map.Entry<String, String> usage : usageRooms.entrySet()) {
                    roomsBundle.putString(usage.getKey(), usage.getValue());
                }
            }
            dest.writeBundle(roomsBundle);
        }

        public static class Deserializer implements JsonDeserializer<Payload> {
            @Override
            public Payload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                Gson gson = new Gson();
                Payload payload = gson.fromJson(json, typeOfT);
                JsonObject jsonObject = json.getAsJsonObject();
                if (jsonObject.has("bindings")) {
                    JsonObject bindings = jsonObject.get("bindings").getAsJsonObject();
                    for (Map.Entry<String, JsonElement> binding : bindings.entrySet()) {
                        String output = binding.getKey();
                        String activity = binding.getValue().isJsonNull() ? "" : binding.getValue().getAsString();
                        payload.deviceBindings.put(output, activity);
                    }
                }
                if (jsonObject.has("params")) {
                    JsonObject params = jsonObject.get("params").getAsJsonObject();
                    for (Map.Entry<String, JsonElement> param : params.entrySet()) {
                        String output = param.getKey();
                        JsonElement outputSettings = param.getValue();
                        deserializeSettings(output, outputSettings, payload);
                    }
                }
                if (jsonObject.has("usageAddresses")) {
                    payload.usageRooms = new HashMap<>();
                    JsonObject usageRooms = jsonObject.get("usageAddresses").getAsJsonObject();
                    for (Map.Entry<String, JsonElement> usage : usageRooms.entrySet()) {
                        String output = usage.getKey();
                        String room = usage.getValue().isJsonNull() ? "" : usage.getValue().getAsString();
                        payload.usageRooms.put(output, room);
                    }
                }
                return payload;
            }

            private void deserializeSettings(String output, JsonElement settingsElement, Payload payload) {
                if (settingsElement.isJsonNull()) {
                    payload.settings.put(output, null);
                } else {
                    JsonObject jsonSettings = settingsElement.getAsJsonObject();
                    Settings settings = new Settings();
                    for (Map.Entry<String, JsonElement> setting : jsonSettings.entrySet()) {
                        JsonElement value = setting.getValue();
                        if (value.isJsonNull()) {
                            settings.entries.put(setting.getKey(), Double.NaN);
                        } else {
                            try {
                                settings.entries.put(setting.getKey(), value.getAsDouble());
                            } catch (ClassCastException | NumberFormatException e1) {
                                try {
                                    settings.entries.put(setting.getKey(), Double.parseDouble(value.getAsString().replace(",", ".")));
                                } catch (ClassCastException | NumberFormatException e2) {
                                    settings.entries.put(setting.getKey(), Double.NaN);
                                }
                            }
                        }
                    }
                    payload.settings.put(output, settings);
                }
            }
        }

        public static class Settings implements Parcelable {
            public static final Creator<Settings> CREATOR = new Creator<Settings>() {
                public Settings createFromParcel(Parcel source) {
                    return new Settings(source);
                }

                public Settings[] newArray(int size) {
                    return new Settings[size];
                }
            };

            public Map<String, Double> entries = new ConcurrentHashMap<>();

            public Settings() {
                // Needed by Retrofit
            }

            protected Settings(Parcel in) {
                Bundle bundle = in.readBundle();
                for (String key : bundle.keySet()) {
                    entries.put(key, bundle.getDouble(key));
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                Bundle bundle = new Bundle();
                for (Map.Entry<String, Double> entry : entries.entrySet()) {
                    Double value = entry.getValue();
                    if (value == null) value = Double.NaN;
                    bundle.putDouble(entry.getKey(), value);
                }
                dest.writeBundle(bundle);
            }
        }

        public static class Repeater implements Parcelable {
            public static final Creator<Repeater> CREATOR = new Creator<Repeater>() {
                public Repeater createFromParcel(Parcel source) {
                    return new Repeater(source);
                }

                public Repeater[] newArray(int size) {
                    return new Repeater[size];
                }
            };
            @SerializedName("deviceId")
            public String id;
            public String floor;

            public Repeater() {
                // Needed by Retrofit
            }

            protected Repeater(Parcel in) {
                id = in.readString();
                floor = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(id);
                dest.writeString(floor);
            }
        }
    }

    public static class Deserializer implements JsonDeserializer<ITAction> {
        @Override
        public ITAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().registerTypeAdapter(Payload.class, new Payload.Deserializer()).create();
            ITAction action = gson.fromJson(json, typeOfT);
            JsonElement order = json.getAsJsonObject().get("order");
            action.index = order != null ? order.getAsInt() - 1 : 0;
            return action;
        }
    }
}
