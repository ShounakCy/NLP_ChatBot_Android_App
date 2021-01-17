package com.projects.shounak.chatbotv3;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetDataService {

    @POST("/query/service")
    Call<TempResponse> geSignTempCall(@Body TempRequest signupRequest);

}
