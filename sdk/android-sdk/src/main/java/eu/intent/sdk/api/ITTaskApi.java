package eu.intent.sdk.api;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITAction;
import eu.intent.sdk.model.ITOperationList;
import eu.intent.sdk.model.ITPart;
import eu.intent.sdk.model.ITTask;
import eu.intent.sdk.model.ITTaskList;
import eu.intent.sdk.model.ITTaskTemplate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A wrapper to call the Task API.
 */
public class ITTaskApi {
    private Service mService;

    public ITTaskApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Gets a list of all the operations.
     */
    public void getOperations(ITApiCallback<ITOperationList> callback) {
        mService.getOperations().enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves all the tasks visible to the user and not finished.
     */
    public void getTasks(ITApiCallback<ITTaskList> callback) {
        mService.getTasks((Integer) null).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves all the tasks visible to the user.
     *
     * @param finishedSince this request will also return the tasks finished since this number of days
     */
    public void getTasks(int finishedSince, ITApiCallback<ITTaskList> callback) {
        mService.getTasks(24 * finishedSince).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the task with the given ID.
     */
    public void getTask(String taskId, ITApiCallback<ITTask> callback) {
        mService.getTask(taskId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts related to the given task.
     */
    public void getParts(String taskId, ITApiCallback<List<ITPart>> callback) {
        mService.getParts(taskId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the task templates.
     */
    public void getTemplates(ITApiCallback<List<ITTaskTemplate>> callback) {
        mService.getTemplates().enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Updates the given action.
     *
     * @param taskId the ID of the task whose action belongs to
     * @param action the action to update
     */
    public void updateAction(String taskId, ITAction action, ITApiCallback<Void> callback) {
        mService.updateAction(taskId, new Service.UpdateAction(taskId, action)).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("v1/operations?beginIndex=0&number=9999")
        Call<ITOperationList> getOperations();

        @GET("v1/my-tasks")
        Call<ITTaskList> getTasks(@Query("since") Integer finishedSince);

        @GET("v1/tasks/{id}")
        Call<ITTask> getTask(@Path("id") String taskId);

        @GET("v1/tasks/{id}/parts")
        Call<List<ITPart>> getParts(@Path("id") String taskId);

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
                            // Replace any NaN value by null (NaN is not supported by JsonWriter)
                            for (Map.Entry<String, Double> setting : settings.entries.entrySet()) {
                                if (Double.isNaN(setting.getValue())) {
                                    settings.entries.put(setting.getKey(), null);
                                }
                            }
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
}
