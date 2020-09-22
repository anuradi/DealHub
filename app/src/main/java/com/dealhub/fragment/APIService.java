package com.dealhub.fragment;

import com.dealhub.notifications.MyResponse;
import com.dealhub.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA903WkiI:APA91bEOOgMACVenylPD2Dy1Jz9xUMBKY4N5rkyDJbdR0rSPxcLuZdOrswzdVQ20ME6-_VTwqm9IWJKNeNgrz2_i2P94LDGy5MidDwvnR4P_NkcQkrFDv57vpkenJ7wD2Q24o07bUjEZ"
            }
    )

    @POST("fcm/send")
    Call<MyResponse>sendNotification(@Body Sender body);
}
