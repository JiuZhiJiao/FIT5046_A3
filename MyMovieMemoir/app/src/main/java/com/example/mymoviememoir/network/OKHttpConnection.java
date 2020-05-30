package com.example.mymoviememoir.network;

import android.util.Log;

import com.example.mymoviememoir.model.Cinema;
import com.example.mymoviememoir.model.Credential;
import com.example.mymoviememoir.model.Memoir;
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

    private static final String BASE_MOVIEDB_URL = "https://api.themoviedb.org/3/search/movie?api_key=ad2a356160e5d42e7258fffe2f7f9f33&language=en-US&query=";

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

    public String findByCredentialsid (String id) {
        final String path = "mymoviememoir.person/findByCredentialsid/" + id;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL+path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.findByCredentialsid method runs wrong");
        }
        return results;
    }

    public String findByPersonId (String id) {
        final String path = "mymoviememoir.memoir/findByPersonid/" + id;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL+path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.findByPersonId method runs wrong");
        }
        return results;
    }

    public String getAllCinema() {
        final String path = "mymoviememoir.cinema/";

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL+path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.getAllCinema method runs wrong");
        }
        return results;
    }

    public String findTopFiveMovies(String id) {
        final String path = "mymoviememoir.memoir/topFiveMovies/" + id;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL+path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.findTopFiveMovies method runs wrong");
        }
        return results;
    }

    public String totalNumberByPostcode(String id, String start, String end) {
        final String path = "mymoviememoir.memoir/totalNumberByCinemaPostcode/"+id+"/"+start+"/"+end;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL+path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.totalNumberByPostcode method runs wrong");
        }
        return results;
    }

    public String totalNumberByWatchedMonth(String id, String year) {
        final String path = "mymoviememoir.memoir/totalNumberByWatchedMonth/"+id+"/"+year;

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL+path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.totalNumberByWatchedMonth method runs wrong");
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

    public String getCountMemoir() {
        final String path = "mymoviememoir.memoir/count";

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

    public String addMemoir(String[] details) {
        Cinema cinema = new Cinema(Integer.parseInt(details[0]),details[1],details[2]);
        Credential credential = new Credential(Integer.parseInt(details[3]),details[4],details[5],details[6]);
        Person person = new Person(Integer.parseInt(details[7]),details[8],details[9],details[10],details[11],details[12],details[13],details[14],credential);
        Memoir memoir = new Memoir(Integer.parseInt(details[15]),details[16],details[17],details[18],details[19],Double.parseDouble(details[20]),person,cinema);

        Gson gson = new Gson();
        String json = gson.toJson(memoir);
        String strResponse = "";

        Log.i("addMemoir ",json);

        final String path = "mymoviememoir.memoir/";
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

    public String addCinema(String[] cinema) {
        Cinema cin = new Cinema(Integer.parseInt(cinema[0]),cinema[1],cinema[2]);

        Gson gson = new Gson();
        String json = gson.toJson(cin);
        String strResponse = "";

        Log.i("addCinema", json);

        final String path = "mymoviememoir.cinema/";
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


    public String searchByName(String movieName) {
        final String path = movieName+"&page=1&include_adult=false";

        Request.Builder builder = new Request.Builder();
        builder.url(BASE_MOVIEDB_URL+path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.totalNumberByWatchedMonth method runs wrong");
        }
        return results;
    }

    public String getGenreCountryById(String id) {
        final String path = "https://api.themoviedb.org/3/movie/"+id+"?api_key=ad2a356160e5d42e7258fffe2f7f9f33&language=en-US";

        Request.Builder builder = new Request.Builder();
        builder.url(path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.totalNumberByWatchedMonth method runs wrong");
        }
        return results;
    }

    public String getCastDirectorById(String id) {
        final String path = "https://api.themoviedb.org/3/movie/"+id+"/credits?api_key=ad2a356160e5d42e7258fffe2f7f9f33";

        Request.Builder builder = new Request.Builder();
        builder.url(path);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OKHttpConnection.totalNumberByWatchedMonth method runs wrong");
        }
        return results;
    }


}
