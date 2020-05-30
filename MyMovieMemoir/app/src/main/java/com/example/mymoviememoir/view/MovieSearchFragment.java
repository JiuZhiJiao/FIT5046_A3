package com.example.mymoviememoir.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.Movie;
import com.example.mymoviememoir.network.OKHttpConnection;

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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class MovieSearchFragment extends Fragment {

    OKHttpConnection okHttpConnection = null;
    int id;
    String name;
    String date;
    String imagePath;
    String summary;
    double score;
    ArrayList<Movie> movies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_movie_search, container, false);
        getActivity().setTitle("Movie Search");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        okHttpConnection = new OKHttpConnection();
        id = 0;
        name = "";
        date = "";
        imagePath = "";
        summary = "";
        score = 0;
        movies = new ArrayList<>();

        /*
        // get data

        // Set list view
        String[] colHead = new String[] {"image","moviename","releaseyear"};
        int[] dataCell = new int[] {R.id.movie_search_image,R.id.movie_search_tv_name,R.id.movie_search_tv_date};
        ListView listView = getActivity().findViewById(R.id.movie_search_list_view);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), null, R.layout.screen_movie_search_listview,colHead,dataCell);
        listView.setAdapter(simpleAdapter);

         */
        final EditText editTextMovie = getActivity().findViewById(R.id.movie_search_et_name);
        Button buttonSearch = getActivity().findViewById(R.id.movie_search_bt_search);
        final SearchByName searchByName = new SearchByName();
        final ListView listView = getActivity().findViewById(R.id.movie_search_list_view);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add search result to movies
                String movieName = editTextMovie.getText().toString().trim();
                movieName = movieName.replace(" ","%20");
                String data = "";
                try {
                    data = searchByName.execute(movieName).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                addMovie(data);


                for (Movie m: movies) {
                    System.out.println(m.getName()+" "+m.getId() +" "+m.getImagePath());
                }

                MyAdapter myAdapter = new MyAdapter(getActivity(),movies,listView);
                listView.setAdapter(myAdapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Movie movie = movies.get(position);
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MessageFromSearch", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name",movie.getName());
                        editor.putString("release",movie.getDate());
                        editor.putString("imagePath",movie.getImagePath());
                        editor.putString("summary",movie.getSummary());
                        editor.putString("score",movie.getScore().toString());
                        editor.putInt("id",movie.getId());
                        editor.apply();
                        MovieViewFragment movieViewFragment = new MovieViewFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("sourceFrom","MovieSearch");
                        movieViewFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_content_frame,movieViewFragment).commit();
                      }
                });

                /*
                HashMap<String, Object> hashMap = new HashMap<>();
                List<HashMap<String, Object>> movieDetail = new ArrayList<>();
                for (Movie m: movies) {
                    hashMap.put("image",getBitmapFromUrl(m.getImagePath()));
                    hashMap.put("name",m.getName());
                    hashMap.put("release",m.getDate());
                    movieDetail.add(hashMap);
                }


                ListView listView = getActivity().findViewById(R.id.movie_search_list_view);
                String[] colHead = new String[] {"image","name","release"};
                int[] dataCell = new int[] {R.id.movie_search_image,R.id.movie_search_lv_tv_name,R.id.movie_search_lv_tv_date};
                SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), movieDetail, R.layout.screen_movie_search_listview,colHead,dataCell);
                listView.setAdapter(simpleAdapter);

                 */
            }
        });

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
            addMovie(s);
        }
    }

    protected void addMovie(String data) {
        try {
            JSONObject jsonData = new JSONObject(data);
            JSONArray jsonArray = jsonData.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject  jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject != null) {
                    id = jsonObject.optInt("id");
                    name = jsonObject.optString("title");
                    date = jsonObject.optString("release_date");
                    imagePath = jsonObject.optString("poster_path");
                    summary = jsonObject.optString("overview");
                    score = jsonObject.getDouble("vote_average");

                    movies.add(new Movie(id,name,date,imagePath,summary,score));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

        List<Movie> list;
        Context context;
        LayoutInflater inflater;
        ListView listView;

        public MyAdapter(Context context, List<Movie> list, ListView listView) {
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
                convertView = inflater.inflate(R.layout.screen_movie_search_listview,parent,false);
                holder.imageView = convertView.findViewById(R.id.movie_search_image);
                holder.textViewName = convertView.findViewById(R.id.movie_search_lv_tv_name);
                holder.textViewDate = convertView.findViewById(R.id.movie_search_lv_tv_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imageView.setTag(list.get(position).getImagePath());
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
            if (holder.imageView.getTag() != null && holder.imageView.getTag().equals(list.get(position).getImagePath())) {
                new ImageDownload(holder.imageView).execute(list.get(position).getImagePath());
            }
            holder.textViewName.setText(list.get(position).getName());
            holder.textViewDate.setText(list.get(position).getDate());

            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewDate;
    }

    // Toast
    protected void sendToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}
