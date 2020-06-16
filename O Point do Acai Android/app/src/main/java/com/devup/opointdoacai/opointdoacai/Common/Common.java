package com.devup.opointdoacai.opointdoacai.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.devup.opointdoacai.opointdoacai.Model.Request;
import com.devup.opointdoacai.opointdoacai.Remote.APIService;
import com.devup.opointdoacai.opointdoacai.Remote.RetrofitClient;
import com.devup.opointdoacai.opointdoacai.Model.User;

public class Common {

    public static Request currentRequest;

    public static User currentUser;

    public static String PHONE_TEXT = "userPhone";

    public static String FLAG_FIRST_ACCESS =  "0";

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService(){

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);

    }

    public static String convertCodeToStatus(String status) {

        if (status.equals("0")){
            return "Pedido";
        }else if (status.equals("1")){
            return "Aguardando";
        }else if (status.equals("2")){
            return "Saiu para Entrega";
        }else{
            return "Entregue";
        }
    }

    public static final String DELETE = "Excluir";

    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

            if (info != null){

                for (int i = 0; i < info.length; i++){

                    if (info[i].getState() == NetworkInfo.State.CONNECTED){

                        return true;

                    }

                }

            }

        }
        return false;

    }
}
