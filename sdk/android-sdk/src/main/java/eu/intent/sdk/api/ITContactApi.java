package eu.intent.sdk.api;

import java.util.List;

import eu.intent.sdk.model.ITContact;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ITContactApi {
    @GET("residentservices/v1/contacts")
    Call<List<ITContact>> get();

    @POST("residentservices/v1/contacts/add")
    Call<Void> add(@Body ITContact body);

    @POST("residentservices/v1/contacts/remove")
    Call<Void> delete(@Body ITContact body);
}