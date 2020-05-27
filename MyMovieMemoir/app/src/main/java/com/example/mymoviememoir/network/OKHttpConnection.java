package com.example.mymoviememoir.network;

import android.util.Log;

import com.example.mymoviememoir.model.Credential;
import com.example.mymoviememoir.model.Person;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

    public String getCountCredentials() {
        final String path = "mymoviememoir.credentials/count";

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL+path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.getCountCredentials method runs wrong");
        }
        return results;
    }

    public String getCountPerson() {
        final String path = "mymoviememoir.person/count";

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL+path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.getCountPerson method runs wrong");
        }
        return results;
    }

    public String addCredential(String[] details) {
        Credential credential = new Credential(Integer.parseInt(details[0]),details[1],details[2],details[3]);

        Gson gson = new Gson();
        String json = gson.toJson(credential);
        String strResponse = "";

        Log.i("json ",json);

        final String path = "mymoviememoir.credentials/";
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(BASE_URL+path).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strResponse;
    }

    public String addPerson(String[] person) {
        Credential cre = new Credential(Integer.parseInt(person[8]),person[9],person[10],person[11]);
        Person per = new Person(Integer.parseInt(person[0]),person[1],person[2],person[3],person[4],person[5],person[6],person[7],cre);

        Gson gson = new Gson();
        String json = gson.toJson(per);
        String strResponse = "";

        Log.i("json ",json);

        final String path = "mymoviememoir.person/";
        RequestBody requestBody = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(BASE_URL+path).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strResponse;
    }


}
