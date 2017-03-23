package eu.intent.sdk.api;

import eu.intent.sdk.model.ITTagList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ITTagApi {
    @GET("datahub/v1/users/system/classifications/tags")
    Call<ITTagList> get();
}