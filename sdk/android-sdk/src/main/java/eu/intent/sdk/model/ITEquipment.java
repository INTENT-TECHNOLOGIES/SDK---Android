package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    private static Service sService;

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

    /**
     * Retrieves the equipments of the given part.
     *
     * @param partId the ITPart's ID
     */
    public static void getByPartId(Context context, String partId, ITApiCallback<ITEquipmentList> callback) {
        getServiceInstance(context).getByAssetId(ITAssetType.PART.toString(), partId, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given part and matching one of the given types.
     *
     * @param partId the ITPart's ID
     * @param types  an array of Types to filter the result
     */
    public static void getByPartId(Context context, String partId, List<Type> types, ITApiCallback<ITEquipmentList> callback) {
        String[] typeNames = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeNames[i] = types.get(i).name();
        }
        getServiceInstance(context).getByAssetId(ITAssetType.PART.toString(), partId, typeNames, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given part.
     *
     * @param partRef the ITPart's external ref
     */
    public static void getByPartRef(Context context, String partRef, ITApiCallback<ITEquipmentList> callback) {
        getServiceInstance(context).getByAssetRef(ITAssetType.PART.toString(), partRef, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given part and matching one of the given types.
     *
     * @param partRef the ITPart's external ref
     * @param types   an array of Types to filter the result
     */
    public static void getByPartRef(Context context, String partRef, List<Type> types, ITApiCallback<ITEquipmentList> callback) {
        String[] typeNames = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeNames[i] = types.get(i).name();
        }
        getServiceInstance(context).getByAssetRef(ITAssetType.PART.toString(), partRef, typeNames, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given site.
     *
     * @param siteId the ITSite's ID
     */
    public static void getBySiteId(Context context, String siteId, ITApiCallback<ITEquipmentList> callback) {
        getServiceInstance(context).getByAssetId(ITAssetType.SITE.toString(), siteId, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given site and matching one of the given types.
     *
     * @param siteId the ITSite's ID
     * @param types  an array of Types to filter the result
     */
    public static void getBySiteId(Context context, String siteId, List<Type> types, ITApiCallback<ITEquipmentList> callback) {
        String[] typeNames = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeNames[i] = types.get(i).name();
        }
        getServiceInstance(context).getByAssetId(ITAssetType.SITE.toString(), siteId, typeNames, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given site.
     *
     * @param siteRef the ITSite's external ref
     */
    public static void getBySiteRef(Context context, String siteRef, ITApiCallback<ITEquipmentList> callback) {
        getServiceInstance(context).getByAssetRef(ITAssetType.SITE.toString(), siteRef, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given site and matching one of the given types.
     *
     * @param siteRef the ITSite's external ref
     * @param types   an array of Types to filter the result
     */
    public static void getBySiteRef(Context context, String siteRef, List<Type> types, ITApiCallback<ITEquipmentList> callback) {
        String[] typeNames = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeNames[i] = types.get(i).name();
        }
        getServiceInstance(context).getByAssetRef(ITAssetType.SITE.toString(), siteRef, typeNames, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipment with the given ID.
     *
     * @param equipmentId the equipment ID
     */
    public static void getById(Context context, String equipmentId, ITApiCallback<ITEquipment> callback) {
        getServiceInstance(context).get(equipmentId, true, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipment with the given reference.
     *
     * @param equipmentRef the equipment reference
     */
    public static void getByRef(Context context, String equipmentRef, ITApiCallback<ITEquipment> callback) {
        getServiceInstance(context).get(equipmentRef, false, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
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

    private interface Service {
        @GET("datahub/v1/equips")
        Call<ITEquipmentList> getByAssetId(@Query("assetType") String assetType, @Query("assetId") String assetId, @Query("type") String[] types, @Query("page") int page, @Query("countByPage") int count, @Query("lang") String lang);

        @GET("datahub/v1/equips")
        Call<ITEquipmentList> getByAssetRef(@Query("assetType") String assetType, @Query("assetRef") String assetRef, @Query("type") String[] types, @Query("page") int page, @Query("countByPage") int count, @Query("lang") String lang);

        @GET("datahub/v1/equips/{externalRef}")
        Call<ITEquipment> get(@Path("externalRef") String partRefOrId, @Query("byId") boolean useId, @Query("lang") String lang);
    }
}
