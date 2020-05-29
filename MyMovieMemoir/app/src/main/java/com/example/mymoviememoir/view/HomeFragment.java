package com.example.mymoviememoir.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.Credential;
import com.example.mymoviememoir.model.Memoir;
import com.example.mymoviememoir.model.Person;
import com.example.mymoviememoir.network.OKHttpConnection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    OKHttpConnection okHttpConnection = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_home, container, false);
        getActivity().setTitle("Home");
        return view;
    }

    Credential credential;
    Person person;
    List<Memoir> memoirs;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        credential = new Credential();
        person = new Person();
        memoirs = new ArrayList<>();
        okHttpConnection = new OKHttpConnection();
        FindByCredentialId findByCredentialId = new FindByCredentialId();
        //FindByPersonId findByPersonId = new FindByPersonId();
        FindTopMovie findTopMovie = new FindTopMovie();

        // get credential and person from sign in and sign up and set person and credential
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle.getParcelable("credentialFromSignIn") != null) {
            credential = bundle.getParcelable("credentialFromSignIn");
            String result = "";
            try {
                result = findByCredentialId.execute(Integer.toString(credential.getCredentialsid())).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            Person[] people = gson.fromJson(result, Person[].class);
            person = people[0];
        } else {
            credential = bundle.getParcelable("credentialFromSignUp");
            person = bundle.getParcelable("personFromSignUp");
        }

        /*
        // Get Memoir object from NetBean
        String memoirStr = "";
        try {
            memoirStr = findByPersonId.execute(Integer.toString(person.getPersonid())).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        memoirs = gson.fromJson(memoirStr, new TypeToken<List<Memoir>>(){}.getType());

         */

        // Set top5 movie
        String topFiveStr = "";
        try {
            topFiveStr = findTopMovie.execute(Integer.toString(person.getPersonid())).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        ArrayList<Map<String,Object>> topMaps = gson.fromJson(topFiveStr, new TypeToken<ArrayList<Map<String,Object>>>(){}.getType());

        ArrayList<HashMap<String,Object>> movieList = new ArrayList<>();
        for (Map<String,Object> m: topMaps) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("movieName", m.get("moviename"));
            map.put("releaseYear", m.get("releaseyear"));
            map.put("ratingScore", m.get("ratingscore"));
            movieList.add(map);
        }

        ListView listView = getActivity().findViewById(R.id.home_list_view);
        String[] colHead = new String[] {"movieName","releaseYear","ratingScore"};
        int[] dataCell = new int[] {R.id.home_lv_tv_name,R.id.home_lv_tv_release_date,R.id.home_lv_tv_score};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), movieList, R.layout.screen_home_listview,colHead,dataCell);
        listView.setAdapter(simpleAdapter);



        // Initial UI
        TextView textViewHi = getActivity().findViewById(R.id.home_tv_hi);
        TextView textViewDate = getActivity().findViewById(R.id.home_tv_current_date);
        textViewHi.setText("Hi, " + person.getFirstname());
        textViewDate.setText(currentDate());



        // pass bundle between fragments

    }

    // find user by credential id
    private class FindByCredentialId extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            return okHttpConnection.findByCredentialsid(id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    /*
    // find memoir by person id
    private class FindByPersonId extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            return okHttpConnection.findByPersonId(id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

     */

    // find top five
    private class FindTopMovie extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            return okHttpConnection.findTopFiveMovies(id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    // Get Sign Up Date (Current Date)
    public String currentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date).toString();
    }
}
