package com.devup.opointdoacai.opointdoacai;

import android.os.AsyncTask;

import com.devup.opointdoacai.opointdoacai.Model.Address;
import com.devup.opointdoacai.opointdoacai.Remote.JsonRequest;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

public class AddressRequest extends AsyncTask<Void, Void, Address> {

    private WeakReference<Cart> activity;

    public AddressRequest(Cart activity ){
        this.activity = new WeakReference<>( activity );
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Address doInBackground(Void... voids) {

        try{
            String jsonString = JsonRequest.request( activity.get().getUriRequest() );
            Gson gson = new Gson();

            return gson.fromJson(jsonString, Address.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Address address) {
        super.onPostExecute(address);

        if( address != null ){
            activity.get().setAddressFields(address);
        }

    }

}
