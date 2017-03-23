package eu.intent.sdk.api;

import java.util.List;

import eu.intent.sdk.model.ITNews;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ITNewsApi {
    @GET("residentservices/v1/news/mine")
    Call<List<ITNews>> get(@Query("category") String categories, @Query("startTime") String startTime, @Query("endTime") String endTime);
}
