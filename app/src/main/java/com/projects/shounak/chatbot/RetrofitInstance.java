package com.projects.shounak.chatbotv3;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
  // private static final String BASE_URL = "http://192.168.43.107:8080"; //
//    private static final String BASE_URL = "http://10.216.90.81:80"; //
     // private static final String BASE_URL = "http://104.211.219.197:8080";
  private static final String BASE_URL = "http://xxx.xxx.com:8080";

    /**
     * Create an instance of Retrofit object
     * */

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            retrofit = new retrofit2.Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

