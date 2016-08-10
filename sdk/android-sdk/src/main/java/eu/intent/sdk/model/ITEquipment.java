package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

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
    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITEquipment() {
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
        custom = in.readBundle();
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
        dest.writeBundle(custom);
    }

    public enum Type {
        CMV, COMMUNITY_BOILER, ELECTRIC_SOLAR_WATER_HEATER, ELECTRIC_WATER_HEATER, FORCED_AIR_FLUE, FORCED_AIR_GAS_WATER_HEATER, FORCED_DRAFT_GAS_BOILER, GAS_CONDENSING_BOILER, GAS_SMOKE_FLUE, GAS_WATER_HEATER, HEATING_CIRCUIT, INDIVIDUAL_BOILER, LIFT, NATURAL_DRAFT_FLUE, NATURAL_DRAFT_GAS_BOILER, NATURAL_DRAFT_GAS_WATER_HEATER, PART_AIR_VENT, PART_FORCED_AIR_FAN, ROOM_SEALED_GAS_BOILER, SECURITY, SITE_FAN, SITE_GAS_FAN, SOLAR_GAS_BOILER, SUBMETER, WATERMETER, UNKNOWN
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
}
