package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A task is something that must be done by a contractor for a client. A task can be part of an ITOperation. It implements a task template which defines its steps. These steps are represented as an array of ITAction.
 *
 * @see ITAction
 */
public class ITTask implements Parcelable {
    public static final Parcelable.Creator<ITTask> CREATOR = new Parcelable.Creator<ITTask>() {
        public ITTask createFromParcel(Parcel source) {
            return new ITTask(source);
        }

        public ITTask[] newArray(int size) {
            return new ITTask[size];
        }
    };

    private static Service sService;

    public List<ITAction> actions = new ArrayList<>();
    @SerializedName("owner")
    public String client;
    @SerializedName("installer")
    public String contractor;
    @SerializedName("step")
    public int currentStep;
    @SerializedName("_id")
    public String id;
    @SerializedName("updated")
    public long lastUpdate;
    public ITOperation operation;
    public boolean selectAssetOnInstall;
    @SerializedName("totalStep")
    public int stepsCount;
    @SerializedName("taskTemplateId")
    public String templateId;

    transient public ITAddress address;
    transient public String assetId;
    transient public ITAssetType assetType;
    transient public String comment;
    transient public boolean commonPart;
    transient public Data data = new Data();
    transient public String door;
    transient public String floor;
    transient public String[] keywords;
    transient public String siteId;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITTask() {
    }

    protected ITTask(Parcel in) {
        actions = new ArrayList<>();
        in.readList(actions, ITAction.class.getClassLoader());
        client = in.readString();
        contractor = in.readString();
        currentStep = in.readInt();
        id = in.readString();
        lastUpdate = in.readLong();
        operation = in.readParcelable(ITOperation.class.getClassLoader());
        selectAssetOnInstall = in.readByte() > 0;
        stepsCount = in.readInt();
        templateId = in.readString();
        address = in.readParcelable(ITAddress.class.getClassLoader());
        assetId = in.readString();
        int tmpAssetType = in.readInt();
        assetType = tmpAssetType == -1 ? null : ITAssetType.values()[tmpAssetType];
        comment = in.readString();
        commonPart = in.readByte() > 0;
        data = in.readParcelable(Data.class.getClassLoader());
        door = in.readString();
        floor = in.readString();
        keywords = in.createStringArray();
        siteId = in.readString();
        custom = in.readBundle();
    }

    /**
     * Retrieves all the tasks visible to the user and not finished.
     */
    public static void get(Context context, ITApiCallback<ITTaskList> callback) {
        getServiceInstance(context).get((Integer) null).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves all the tasks visible to the user.
     *
     * @param finishedSince this request will also return the tasks finished since this number of days
     */
    public static void get(Context context, int finishedSince, ITApiCallback<ITTaskList> callback) {
        getServiceInstance(context).get(24 * finishedSince).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the task with the given ID.
     */
    public static void get(Context context, String taskId, ITApiCallback<ITTask> callback) {
        getServiceInstance(context).get(taskId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the task templates.
     */
    public static void getTemplates(Context context, ITApiCallback<List<ITTaskTemplate>> callback) {
        getServiceInstance(context).getTemplates().enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Updates the given action.
     *
     * @param taskId the ID of the task whose action belongs to
     * @param action the action to update
     */
    public static void updateAction(Context context, String taskId, ITAction action, ITApiCallback<Void> callback) {
        getServiceInstance(context).updateAction(taskId, new Service.UpdateAction(taskId, action)).enqueue(new ProxyCallback<>(callback));
    }

    private static Service getServiceInstance(Context context) {
        if (sService == null) {
            sService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
        }
        return sService;
    }

    /**
     * Returns true if at least an action of this task is marked with the error flag, false otherwise.
     */
    public boolean hasError() {
        for (ITAction action : actions) {
            if (action.error) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the task is finished, i.e. the current step is after the last step.
     */
    public boolean isFinished() {
        return currentStep >= stepsCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(actions);
        dest.writeString(client);
        dest.writeString(contractor);
        dest.writeInt(currentStep);
        dest.writeString(id);
        dest.writeLong(lastUpdate);
        dest.writeParcelable(operation, flags);
        dest.writeByte((byte) (selectAssetOnInstall ? 1 : 0));
        dest.writeInt(stepsCount);
        dest.writeString(templateId);
        dest.writeParcelable(address, 0);
        dest.writeString(assetId);
        dest.writeInt(assetType == null ? -1 : assetType.ordinal());
        dest.writeString(comment);
        dest.writeByte((byte) (commonPart ? 1 : 0));
        dest.writeParcelable(data, flags);
        dest.writeString(door);
        dest.writeString(floor);
        dest.writeStringArray(keywords);
        dest.writeString(siteId);
        dest.writeBundle(custom);
    }

    private interface Service {
        @GET("v1/my-tasks")
        Call<ITTaskList> get(@Query("since") Integer finishedSince);

        @GET("v1/tasks/{id}")
        Call<ITTask> get(@Path("id") String taskId);

        @GET("v1/tasks/templates")
        Call<List<ITTaskTemplate>> getTemplates();

        @POST("v1/tasks/{taskId}/action")
        Call<Void> updateAction(@Path("taskId") String taskId, @Body UpdateAction action);

        class UpdateAction {
            public String taskId;
            public Action action;

            UpdateAction(String taskId, ITAction action) {
                this.taskId = taskId;
                this.action = new Action(action);
            }

            class Action {
                public String actionTemplateId;
                public boolean error;
                public boolean finished;
                public String comment;
                public Object payload;

                Action(ITAction action) {
                    this.actionTemplateId = action.templateId;
                    this.error = action.error;
                    this.finished = action.finished;
                    this.comment = action.comment;
                    switch (action.templateId) {
                        case "install":
                            this.payload = new PayloadInstall(action.payload);
                            break;
                        case "repeater":
                            this.payload = new PayloadRepeaters(action.payload);
                            break;
                        case "settings":
                            this.payload = new PayloadSettings(action.payload);
                            break;
                    }
                }

                class PayloadInstall {
                    public String assetId;
                    public String deviceId;
                    public String level;
                    public String room;
                    @SerializedName("connection_point")
                    public String connectionPoint;
                    public Map<String, String> bindings;
                    public Map<String, String> usageAddresses;

                    PayloadInstall(ITAction.Payload payload) {
                        this.assetId = payload.assetId;
                        this.deviceId = payload.deviceId;
                        this.level = payload.floor;
                        this.room = payload.installRoom;
                        this.connectionPoint = payload.connectionPoint;
                        this.bindings = payload.deviceBindings;
                        this.usageAddresses = payload.usageRooms;
                    }
                }

                class PayloadSettings {
                    public Map<String, Map<String, Double>> params;

                    PayloadSettings(ITAction.Payload payload) {
                        this.params = new HashMap<>();
                        for (Map.Entry<String, ITAction.Payload.Settings> entry : payload.settings.entrySet()) {
                            String output = entry.getKey();
                            ITAction.Payload.Settings settings = entry.getValue();
                            this.params.put(output, settings.entries);
                        }
                    }
                }

                class PayloadRepeaters {
                    public List<Repeater> repeaters;

                    PayloadRepeaters(ITAction.Payload payload) {
                        this.repeaters = new ArrayList<>();
                        for (ITAction.Payload.Repeater repeater : payload.repeaters) {
                            this.repeaters.add(new Repeater(repeater));
                        }
                    }

                    class Repeater {
                        public String deviceId;
                        public String floor;

                        Repeater(ITAction.Payload.Repeater repeater) {
                            this.deviceId = repeater.id;
                            this.floor = repeater.floor;
                        }
                    }
                }
            }
        }
    }

    /**
     * A task can provide additional data. Not all the properties of the Data object may be set, depending on the task template.
     */
    public static class Data implements Parcelable {
        public static final Creator<Data> CREATOR = new Creator<Data>() {
            public Data createFromParcel(Parcel source) {
                return new Data(source);
            }

            public Data[] newArray(int size) {
                return new Data[size];
            }
        };
        public List<String> activities;
        public String deviceId;
        public String deviceType;

        public Data() {
        }

        protected Data(Parcel in) {
            activities = in.createStringArrayList();
            deviceId = in.readString();
            deviceType = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringList(activities);
            dest.writeString(deviceId);
            dest.writeString(deviceType);
        }
    }

    public static class Deserializer implements JsonDeserializer<ITTask> {
        @Override
        public ITTask deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ITAction.class, new ITAction.Deserializer())
                    .registerTypeAdapter(ITLocation.class, new ITLocation.Deserializer())
                    .create();
            ITTask task = gson.fromJson(json, typeOfT);
            Collections.sort(task.actions, new Comparator<ITAction>() {
                @Override
                public int compare(ITAction a1, ITAction a2) {
                    return Integer.valueOf(a1.index).compareTo(a2.index);
                }
            });
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement asset = jsonObject.get("asset");
            if (asset != null && asset.isJsonObject()) {
                JsonObject assetObject = asset.getAsJsonObject();
                task.address = gson.fromJson(assetObject.get("address"), ITAddress.class);
                task.assetId = assetObject.has("assetId") && !assetObject.get("assetId").isJsonNull() ? assetObject.get("assetId").getAsString() : "";
                task.assetType = assetObject.has("assetType") && !assetObject.get("assetType").isJsonNull() ?
                        ITAssetType.fromString(assetObject.get("assetType").getAsString()) :
                        ITAssetType.SITE;
                task.commonPart = !assetObject.has("portion") || TextUtils.isEmpty(assetObject.get("portion").getAsString()) || TextUtils.equals(assetObject.get("portion").getAsString(), "commonPortion");
                task.door = assetObject.has("door") && !assetObject.get("door").isJsonNull() ? assetObject.get("door").getAsString() : "";
                task.floor = assetObject.has("level") && !assetObject.get("level").isJsonNull() ? assetObject.get("level").getAsString() : "";
                task.keywords = assetObject.has("keywords") && !assetObject.get("keywords").isJsonNull() ? assetObject.get("keywords").getAsString().split(" ") : new String[0];
                task.siteId = assetObject.has("siteId") && !assetObject.get("siteId").isJsonNull() ? assetObject.get("siteId").getAsString() : "";
            }
            JsonElement infos = jsonObject.get("infos");
            if (infos != null && infos.isJsonObject()) {
                JsonObject infosObject = infos.getAsJsonObject();
                task.comment = infosObject.has("comment") && !infosObject.get("comment").isJsonNull() ? infosObject.get("comment").getAsString() : "";
                task.data.deviceType = infosObject.has("deviceType") ? infosObject.get("deviceType").getAsString() : "";
                task.data.activities = new ArrayList<>();
                if (infosObject.has("activities")) {
                    for (JsonElement activity : infosObject.get("activities").getAsJsonArray()) {
                        task.data.activities.add(activity.getAsString());
                    }
                }
            }
            return task;
        }
    }
}
