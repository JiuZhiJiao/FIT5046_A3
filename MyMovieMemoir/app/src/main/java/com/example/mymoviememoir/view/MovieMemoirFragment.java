package com.example.mymoviememoir.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.Credential;
import com.example.mymoviememoir.model.Person;
import com.example.mymoviememoir.network.OKHttpConnection;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MovieMemoirFragment extends Fragment {

    OKHttpConnection okHttpConnection = null;
    String netBeanData;
    Credential credential;
    Person person;
    int personId;
    ArrayList<HashMap<String,Object>> memoirs;
    ArrayList<HashMap<String,Object>> fullMemoirs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_movie_memoir, container, false);
        getActivity().setTitle("Movie Memoir");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        okHttpConnection = new OKHttpConnection();
        netBeanData = "";
        credential = new Credential();
        person = new Person();
        personId = 0;
        memoirs = new ArrayList<>();
        fullMemoirs = new ArrayList<>();

        // Get Person
        credential = new Credential();
        person = new Person();
        FindByCredentialId findByCredentialId = new FindByCredentialId();
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
        personId = person.getPersonid();

        // Get all memoir of the current person
        try {
            netBeanData = new FindByPersonId().execute(String.valueOf(personId)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addMemoir(netBeanData);
        setFullMemoir();
        for (HashMap<String,Object> hashMap: fullMemoirs) {
            System.out.println(hashMap.get("imagePath"));
        }

        // Set List
        final ListView listView = getActivity().findViewById(R.id.movie_memoir_list_view);
        MyAdapter myAdapter = new MyAdapter(getActivity(),fullMemoirs,listView);
        listView.setAdapter(myAdapter);

        // Sort from spinner
        Spinner spinner = getActivity().findViewById(R.id.movie_memoir_spinner_sort);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        Collections.sort(fullMemoirs, new WatchedDateSort());
                        listView.setAdapter(new MyAdapter(getActivity(),fullMemoirs,listView));
                        break;
                    case 2:
                        Collections.sort(fullMemoirs, new UserScoreSort());
                        listView.setAdapter(new MyAdapter(getActivity(),fullMemoirs,listView));
                        break;
                    case 3:
                        Collections.sort(fullMemoirs, new PublicScoreSort());
                        listView.setAdapter(new MyAdapter(getActivity(),fullMemoirs,listView));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // List Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final HashMap<String,Object> hashMap = fullMemoirs.get(position);
                final String title = hashMap.get("name").toString();
                String score = "                                   Public Score: "+hashMap.get("publicScore").toString();
                score += "/10";
                String summary = "Overview: "+hashMap.get("summary").toString();
                String[] items = new String[] {score,summary};
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle(title).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Share To Twitter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Share to Twitter
                        String sharedTitle = "Movie Name: " + title + ". ";
                        String sharedScore = "My Personal Score: " + hashMap.get("userScore").toString() + "/5. ";
                        String sharedComment = hashMap.get("comment").toString();
                        String sharedUser = "     ---From: "+person.getFirstname();
                        String sharedInfo = sharedTitle + sharedScore + sharedComment + sharedUser;
                        Twitter.initialize(getActivity());
                        shareToTwitter(view,sharedInfo);
                    }
                }).create();
                alertDialog.show();
            }
        });

    }

    //Send To Twitter
    public void shareToTwitter(View view, String info) {
        // Set Twitter Content
        TweetComposer.Builder builder = new TweetComposer.Builder(getActivity()).text(info);
        builder.show();
    }

    // order
    protected class WatchedDateSort implements Comparator<HashMap<String,Object>> {
        @Override
        public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
            return o1.get("watchedDate").toString().compareTo(o2.get("watchedDate").toString());
        }
    }

    protected class UserScoreSort implements Comparator<HashMap<String,Object>> {
        @Override
        public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
            Double comp1 = Double.parseDouble(o1.get("userScore").toString());
            Double comp2 = Double.parseDouble(o2.get("userScore").toString());
            int result = 0;
            if (comp1 > comp2) {
                result = 1;
            }
            if (comp1 < comp2) {
                result = -1;
            }
            return result;
        }
    }

    protected class PublicScoreSort implements Comparator<HashMap<String,Object>> {
        @Override
        public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
            Double comp1 = Double.parseDouble(o1.get("publicScore").toString());
            Double comp2 = Double.parseDouble(o2.get("publicScore").toString());
            int result = 0;
            if (comp1 > comp2) {
                result = -1;
            }
            if (comp1 < comp2) {
                result = 1;
            }
            return result;
        }
    }


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

    protected void addMemoir(String data) {
        try {
            /*
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("cinemaid");

             */
            JSONArray jsonArrayInfo = new JSONArray(data);
            /*
            JSONArray jsonArrayName = jsonObject.getJSONArray("moviename");
            JSONArray jsonArrayReleaseDate = jsonObject.getJSONArray("releasedate");
            JSONArray jsonArrayWatchedDate = jsonObject.getJSONArray("watchedtime");
            JSONArray jsonArrayComment = jsonObject.getJSONArray("comment");
            JSONArray jsonArrayScore = jsonObject.getJSONArray("ratingscore");

             */

            // get postcode
            for (int i = 0; i < jsonArrayInfo.length(); i++) {
                //JSONObject postcode = jsonArray.getJSONObject(i);
                JSONObject info = jsonArrayInfo.getJSONObject(i);
                JSONObject cinemainfo = info.getJSONObject("cinemaid");
                /*
                JSONObject moviename = jsonArrayName.getJSONObject(i);
                JSONObject releasedate = jsonArrayReleaseDate.getJSONObject(i);
                JSONObject watcheddate = jsonArrayWatchedDate.getJSONObject(i);
                JSONObject comment = jsonArrayComment.getJSONObject(i);
                JSONObject score = jsonArrayScore.getJSONObject(i);

                 */

                if (info != null) {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("postcode",cinemainfo.optString("cinemapostcode"));
                    map.put("moviename",info.optString("moviename"));
                    map.put("releasedate",info.optString("releasedate"));
                    map.put("watcheddate",info.optString("watchedtime"));
                    map.put("comment",info.optString("comment"));
                    map.put("score",info.optDouble("ratingscore"));

                    memoirs.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void setFullMemoir() {
        for (HashMap<String, Object> hashMap: memoirs) {
            String[] strings = getExtraInfo(hashMap.get("moviename").toString());
            HashMap<String,Object> map = new HashMap<>();
            map.put("id",strings[0]);
            map.put("name",hashMap.get("moviename"));
            map.put("imagePath",strings[1]);
            map.put("userScore",hashMap.get("score"));
            map.put("publicScore",strings[2]);
            map.put("postcode",hashMap.get("postcode"));
            String[] release = hashMap.get("releasedate").toString().split("T");
            map.put("releaseDate",release[0]);
            String[] watched = hashMap.get("watcheddate").toString().split("T");
            map.put("watchedDate",watched[0]);
            map.put("comment",hashMap.get("comment"));
            map.put("summary",strings[3]);

            fullMemoirs.add(map);
        }
    }

    private String[] getExtraInfo(String name) {
        String s = "";
        try {
            s = new SearchByName().execute(name).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String id = "";
        String imagePath = "";
        String score = "0";
        String summary = "";
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject info = jsonArray.getJSONObject(0);

            id = info.optString("id");
            imagePath = info.optString("poster_path");
            imagePath = "https://image.tmdb.org/t/p/w600_and_h900_bestv2"+imagePath;
            score = info.optString("vote_average");
            summary = info.optString("overview");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] strings = new String[] {id,imagePath,score,summary};
        return strings;
    }


    private class SearchByName extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            return okHttpConnection.searchByName(name);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private class ImageDownload extends AsyncTask<String, Void, Bitmap> {
        private ImageView myImageView;

        public ImageDownload(ImageView imageView) {
            myImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = getBitmapFromUrl(url);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            myImageView.setImageBitmap(bitmap);
        }
    }

    public Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap;
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };

        try {
            URL myUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
            inputStream = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(inputStream);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // Load Image
    public class MyAdapter extends BaseAdapter {

        ArrayList<HashMap<String,Object>> list;
        Context context;
        LayoutInflater inflater;
        ListView listView;

        public MyAdapter(Context context, ArrayList<HashMap<String,Object>> list, ListView listView) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
            this.listView = listView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.screen_movie_memoir_listview,parent,false);
                holder.imageView = convertView.findViewById(R.id.movie_memoir_image);
                holder.textViewName = convertView.findViewById(R.id.movie_memoir_title);
                holder.textViewReleaseDate = convertView.findViewById(R.id.movie_memoir_release_date);
                holder.textViewWatchedDate = convertView.findViewById(R.id.movie_memoir_watched_date);
                holder.textViewPostcode = convertView.findViewById(R.id.movie_memoir_lv_postcode);
                holder.ratingBar = convertView.findViewById(R.id.movie_memoir_rating_bar);
                holder.textViewComment = convertView.findViewById(R.id.movie_memoir_comment);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imageView.setTag(list.get(position).get("imagePath").toString());
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
            if (holder.imageView.getTag() != null && holder.imageView.getTag().equals(list.get(position).get("imagePath").toString())) {
                new ImageDownload(holder.imageView).execute(list.get(position).get("imagePath").toString());
            }

            holder.textViewName.setText(list.get(position).get("name").toString());
            holder.textViewReleaseDate.setText("Release Date: "+list.get(position).get("releaseDate").toString());
            holder.textViewWatchedDate.setText("Watched Date: "+list.get(position).get("watchedDate").toString());
            holder.textViewPostcode.setText(list.get(position).get("postcode").toString());
            holder.ratingBar.setRating(Float.parseFloat(list.get(position).get("userScore").toString()));
            holder.textViewComment.setText(list.get(position).get("comment").toString());

            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewReleaseDate;
        TextView textViewWatchedDate;
        TextView textViewPostcode;
        RatingBar ratingBar;
        TextView textViewComment;
    }
}
