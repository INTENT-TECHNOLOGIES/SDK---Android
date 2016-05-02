package eu.intent.sdk.model;

import android.content.Context;
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

import java.util.Date;
import java.util.List;
import java.util.Locale;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.util.ITDateUtils;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * This class represents a piece of data, at a given date, with a given value. In a few cases there may be a valueMin and valueMax instead of value.
 */
public class ITData implements Parcelable {
    public static final Parcelable.Creator<ITData> CREATOR = new Parcelable.Creator<ITData>() {
        public ITData createFromParcel(Parcel source) {
            return new ITData(source);
        }

        public ITData[] newArray(int size) {
            return new ITData[size];
        }
    };

    private static Service sService;

    public long timestamp;
    @SerializedName("trustlevel")
    public TrustLevel trustLevel;

    transient public double value = Double.NaN;
    transient public double valueMin = Double.NaN;
    transient public double valueMax = Double.NaN;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITData() {
    }

    protected ITData(Parcel in) {
        timestamp = in.readLong();
        value = in.readDouble();
        int tmpTrustLevel = in.readInt();
        trustLevel = tmpTrustLevel == -1 ? null : TrustLevel.values()[tmpTrustLevel];
        custom = in.readBundle();
    }

    /**
     * Retrieves the data for a part and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param partRef      the ITPart's external ref
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the keys of the concerned activities
     * @param granularity  the data granularity
     * @param startTime    the oldest date limit you're interested in
     * @param endTime      the newest date limit you're interested in
     * @param page         the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public static void getByPartRef(Context context, String partRef, Type dataType, String[] activityKeys, Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        getServiceInstance(context).getByPart(partRef, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, false, Granularity.DAY.equals(granularity)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data for a part and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param partId       the ITPart's Intent id
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the keys of the concerned activities
     * @param granularity  the data granularity
     * @param startTime    the oldest date limit you're interested in
     * @param endTime      the newest date limit you're interested in
     * @param page         the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public static void getByPartId(Context context, String partId, Type dataType, String[] activityKeys, Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        getServiceInstance(context).getByPart(partId, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, true, Granularity.DAY.equals(granularity)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data for a site and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param siteRef      the ITSite's external ref
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the keys of the concerned activities
     * @param startTime    the oldest date limit you're interested in
     * @param endTime      the newest date limit you're interested in
     * @param granularity  the data granularity
     * @param page         the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public static void getBySiteRef(Context context, String siteRef, Type dataType, String[] activityKeys, Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        getServiceInstance(context).getBySite(siteRef, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, false, Granularity.DAY.equals(granularity)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data for a site and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param siteId       the ITSite's Intent id
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the key of the concerned activities
     * @param granularity  the data granularity
     * @param startTime    the oldest date limit you're interested in
     * @param endTime      the newest date limit you're interested in
     * @param page         the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public static void getBySiteId(Context context, String siteId, Type dataType, String[] activityKeys, Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        getServiceInstance(context).getBySite(siteId, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, true, Granularity.DAY.equals(granularity)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data from one or several streams. The result will be a list of ITDataResults, grouped by stream.
     *
     * @param dataType    an ITDataType depending on what you want to retrieve
     * @param streamIds   the ITStreams' IDs
     * @param granularity the data granularity
     * @param startTime   the oldest date limit you're interested in
     * @param endTime     the newest date limit you're interested in
     * @param page        the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public static void getByStream(Context context, Type dataType, String[] streamIds, Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        getServiceInstance(context).getByStream(dataType.toString(), streamIds, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, Granularity.DAY.equals(granularity)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Publishes a piece of data to a part.
     *
     * @param partId      the ITPart's ID
     * @param activityKey the key of the concerned activity
     * @param dataType    an ITDataType depending on what you want to publish
     * @param timestamp   the data timestamp
     * @param value       the data value
     * @param trustLevel  a trust level depending on where (or who) you data comes from
     */
    public static void publishToPartWithId(Context context, String partId, String activityKey, Type dataType, long timestamp, double value, TrustLevel trustLevel, ITApiCallback<Void> callback) {
        getServiceInstance(context).publishToPart(partId, activityKey, dataType.toString(), true, new Service.Body(timestamp, value, trustLevel)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Publishes a piece of data to a part.
     *
     * @param partRef     the ITPart's external ref
     * @param activityKey the key of the concerned activity
     * @param dataType    an ITDataType depending on what you want to publish
     * @param timestamp   the data timestamp
     * @param value       the data value
     * @param trustLevel  a trust level depending on where (or who) you data comes from
     */
    public static void publishToPartWithRef(Context context, String partRef, String activityKey, Type dataType, long timestamp, double value, TrustLevel trustLevel, ITApiCallback<Void> callback) {
        getServiceInstance(context).publishToPart(partRef, activityKey, dataType.toString(), false, new Service.Body(timestamp, value, trustLevel)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Publishes a piece of data to a site.
     *
     * @param siteRef     the ITSite's external ref
     * @param activityKey the key of the concerned activity
     * @param dataType    an ITDataType depending on what you want to publish
     * @param timestamp   the data timestamp
     * @param value       the data value
     * @param trustLevel  a trust level depending on where (or who) you data comes from
     */
    public static void publishToSite(Context context, String siteRef, String activityKey, Type dataType, long timestamp, double value, TrustLevel trustLevel, ITApiCallback<Void> callback) {
        getServiceInstance(context).publishToSite(siteRef, activityKey, dataType.toString(), new Service.Body(timestamp, value, trustLevel)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Publishes a piece of data to a stream.
     *
     * @param streamId   the ITStream's ID
     * @param dataType   an ITDataType depending on what you want to publish
     * @param timestamp  the data timestamp
     * @param value      the data value
     * @param trustLevel a trust level depending on where (or who) you data comes from
     */
    public static void publishToStream(Context context, String streamId, Type dataType, long timestamp, double value, TrustLevel trustLevel, ITApiCallback<Void> callback) {
        getServiceInstance(context).publishToStream(streamId, dataType.toString(), new Service.Body(timestamp, value, trustLevel)).enqueue(new ProxyCallback<>(callback));
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
        dest.writeLong(timestamp);
        dest.writeDouble(value);
        dest.writeInt(trustLevel == null ? -1 : trustLevel.ordinal());
        dest.writeBundle(custom);
    }

    public enum Granularity {
        DAY, HOUR
    }

    public enum Type {
        /**
         * Relative to something that is not normal
         */
        ALERT,
        /**
         * The average of several values on a period of time
         */
        AVERAGE,
        /**
         * The difference between two values on a period of time
         */
        DELTA,
        /**
         * A snapshot of the data at a given time
         */
        SNAPSHOT,
        /**
         * A technical intervention
         */
        TICKET,
        UNKNOWN;

        @Override
        public String toString() {
            return name().toLowerCase(Locale.US);
        }
    }

    public enum TrustLevel {
        @SerializedName("COMPUTED")COMPUTED,
        @SerializedName("ESTIMATE")ESTIMATE,
        @SerializedName("MANUAL")MANUAL,
        @SerializedName("SENSOR")SENSOR,
        @SerializedName("TRUSTED_MANUAL")TRUSTED_MANUAL,
        @SerializedName("TRUSTED_SENSOR")TRUSTED_SENSOR,
        UNKNOWN
    }

    private interface Service {
        int MAX_COUNT = 31 * 24;

        @GET("datahub/v1/parts/{externalRef}/{dataType}")
        Call<List<ITDataResult>> getByPart(@Path("externalRef") String partRef, @Path("dataType") String dataType, @Query("activityKey") String[] activityKeys, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("page") int page, @Query("countByPage") int count, @Query("byId") boolean byId, @Query("dataByDay") boolean dataByDay);

        @GET("datahub/v1/sites/{externalRef}/{dataType}")
        Call<List<ITDataResult>> getBySite(@Path("externalRef") String siteRef, @Path("dataType") String dataType, @Query("activityKey") String[] activityKeys, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("page") int page, @Query("countByPage") int count, @Query("byId") boolean byId, @Query("dataByDay") boolean dataByDay);

        @GET("datahub/v1/streams/{dataType}")
        Call<List<ITDataResult>> getByStream(@Path("dataType") String dataType, @Query("streamId") String[] streamIds, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("page") int page, @Query("countByPage") int count, @Query("dataByDay") boolean dataByDay);

        @POST("datahub/v1/parts/{externalRef}/activities/{activityKey}/{dataType}")
        Call<Void> publishToPart(@Path("externalRef") String partRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @Query("byId") boolean usePartId, @retrofit2.http.Body ITData.Service.Body body);

        @POST("datahub/v1/sites/{externalRef}/activities/{activityKey}/{dataType}")
        Call<Void> publishToSite(@Path("externalRef") String siteRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @retrofit2.http.Body ITData.Service.Body body);

        @POST("datahub/v1/streams/{streamId}/{dataType}")
        Call<Void> publishToStream(@Path("streamId") String streamId, @Path("dataType") String dataType, @retrofit2.http.Body ITData.Service.Body body);

        class Body {
            public long timestamp;
            public double value;
            @SerializedName("trustlevel")
            public TrustLevel trustLevel;

            private Body(long timestamp, double value, TrustLevel trustLevel) {
                this.timestamp = timestamp;
                this.value = value;
                this.trustLevel = trustLevel;
            }
        }
    }

    public static class Deserializer implements JsonDeserializer<ITData> {
        @Override
        public ITData deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new Gson();
            ITData data = gson.fromJson(json, typeOfT);
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement jsonValue = jsonObject.get("value");
            if (jsonValue.isJsonObject()) {
                JsonObject value = jsonObject.getAsJsonObject("value");
                data.valueMin = value.get("min").getAsDouble();
                data.valueMax = value.get("max").getAsDouble();
            } else if (jsonValue.isJsonPrimitive()) {
                data.value = jsonValue.getAsDouble();
            }
            return data;
        }
    }
}
