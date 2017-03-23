package eu.intent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

/**
 * An equipment that can be equipped with a sensor (lift, boiler,...).
 */
public class ITEquipment implements Parcelable {
    public static final Creator<ITEquipment> CREATOR = new Creator<ITEquipment>() {
        public ITEquipment createFromParcel(Parcel source) {
            return new ITEquipment(source);
        }

        public ITEquipment[] newArray(int size) {
            return new ITEquipment[size];
        }
    };

    public ITAddress address;
    public String brand;
    public String externalRef;
    public String id;
    public String label;
    public String model;
    @SerializedName("nature")
    public Scope scope = Scope.UNKNOWN;
    @SerializedName("SN")
    public String serialNumber;
    @SerializedName("inserviceDate")
    public long serviceDate;
    public Type type = Type.UNKNOWN;
    @SerializedName("removalDate")
    public long uninstallDate;

    public ITEquipment() {
        // Needed by Gson
    }

    protected ITEquipment(Parcel in) {
        address = in.readParcelable(ITAddress.class.getClassLoader());
        brand = in.readString();
        externalRef = in.readString();
        id = in.readString();
        label = in.readString();
        model = in.readString();
        int scopeOrdinal = in.readInt();
        scope = scopeOrdinal == -1 ? Scope.UNKNOWN : Scope.values()[scopeOrdinal];
        serialNumber = in.readString();
        serviceDate = in.readLong();
        int typeOrdinal = in.readInt();
        type = typeOrdinal == -1 ? Type.UNKNOWN : Type.values()[typeOrdinal];
        uninstallDate = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(address, flags);
        dest.writeString(brand);
        dest.writeString(externalRef);
        dest.writeString(id);
        dest.writeString(label);
        dest.writeString(model);
        dest.writeInt(scope == null ? -1 : scope.ordinal());
        dest.writeString(serialNumber);
        dest.writeLong(serviceDate);
        dest.writeInt(type == null ? -1 : type.ordinal());
        dest.writeLong(uninstallDate);
    }

    public enum Type {
        CMV,
        COMMUNITY_BOILER,
        ELECTRIC_METER,
        ELECTRIC_SOLAR_WATER_HEATER,
        ELECTRIC_WATER_HEATER,
        FLOAT_WATER,
        FORCED_AIR_FLUE,
        FORCED_AIR_GAS_WATER_HEATER,
        FORCED_DRAFT_GAS_BOILER,
        GAS_CONDENSING_BOILER,
        GAS_SMOKE_FLUE,
        GAS_WATER_HEATER,
        GASMETER,
        GENERIC_PUMP,
        HEATING_CIRCUIT,
        INDIVIDUAL_BOILER,
        LIFT,
        METERING_POINT,
        MODULE_CIC,
        NATURAL_DRAFT_FLUE,
        NATURAL_DRAFT_GAS_BOILER,
        NATURAL_DRAFT_GAS_WATER_HEATER,
        PART_AIR_VENT,
        PART_FORCED_AIR_FAN,
        POWER_SOLAR,
        ROOM_SEALED_GAS_BOILER,
        SECURITY,
        SITE_FAN,
        SITE_GAS_FAN,
        SKYDOME,
        SOLAR_GAS_BOILER,
        SUBMETER,
        SWITCHBOARD,
        WATER_CIRCUIT,
        WATERMETER,
        UNKNOWN
    }

    public enum Scope {
        /**
         * The equipment is related to several assets.
         */
        @SerializedName("common")COMMON,
        /**
         * The equipment is related to one asset only.
         */
        @SerializedName("private")PRIVATE,
        UNKNOWN
    }

    public static class Deserializer implements JsonDeserializer<ITEquipment> {
        @Override
        public ITEquipment deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ITLocation.class, new ITLocation.Deserializer())
                    .create();
            ITEquipment equipment = gson.fromJson(json, typeOfT);
            if (equipment.address == null) {
                equipment.address = new ITAddress();
            }
            if (equipment.scope == null) {
                equipment.scope = Scope.UNKNOWN;
            }
            if (equipment.type == null) {
                equipment.type = Type.UNKNOWN;
            }
            return equipment;
        }
    }
}
