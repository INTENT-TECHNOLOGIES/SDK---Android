package eu.intent.sdk.api;

import eu.intent.sdk.model.ITActivity;
import eu.intent.sdk.model.ITActivityList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITActivityApi {
    @GET("datahub/v1/activities")
    Call<ITActivityList> get(@Query("page") int page, @Query("countByPage") int countByPage, @Query("lang") String lang);

    @GET("datahub/v1/activities/{activityKey}")
    Call<ITActivity> get(@Path("activityKey") String id, @Query("lang") String lang);
}
