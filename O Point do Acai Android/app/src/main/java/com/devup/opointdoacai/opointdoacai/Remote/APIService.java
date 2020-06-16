package com.devup.opointdoacai.opointdoacai.Remote;

import com.devup.opointdoacai.opointdoacai.Model.MyResponse;
import com.devup.opointdoacai.opointdoacai.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {

               "Content-Type:application/json",
                    "Authorization:key=AAAAbt4y9ME:APA91bETHpuYNZ9Z0F-cKbpYKqSeRR2CjUFrklevFcBH7EouaOSBxHtr3qiG7juWmno64uKa4iSMXgdlNq2VDiWqg3ya_7DCYbKDLqXBS-cKrxw3ECTWu_7UFWivLJCwCX6gny8hIANrXcnuge0jKZXv1v_vJKSMHA"

            }
            )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
