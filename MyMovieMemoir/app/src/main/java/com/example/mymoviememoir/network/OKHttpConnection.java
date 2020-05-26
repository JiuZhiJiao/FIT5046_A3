package com.example.mymoviememoir.network;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKHttpConnection {

    private static final String BASE_URL = "http://192.168.0.77:8080/MyMovieMemoir/webresources/";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = null;
    private String results = "";

    public OKHttpConnection() {
        client = new OkHttpClient();
    }

    public String findByUsername (String username) {
        final String path = "mymoviememoir.credentials/findByUsername/" + username;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL+path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.findByUsername method runs wrong");
        }

        return results;

    }


}
