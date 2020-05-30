package com.example.mymoviememoir.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.Movie;
import com.example.mymoviememoir.network.OKHttpConnection;
import com.google.gson.JsonArray;

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
import java.util.List;

import okhttp3.OkHttpClient;

public class MovieViewFragment extends Fragment {

    OKHttpConnection okHttpConnection = null;
    SharedPreferences sharedPreferences = null;
    String name;
    String release;
    String imagePath;
    String summary;
    Double score;
    int id;
    List<String> genre;
    String country;
    String sourceFrom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_movie_view,container,false);
        getActivity().setTitle("Movie View");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        okHttpConnection = new OKHttpConnection();
        name = "";
        release = "";
        imagePath = "";
        summary = "";
        score = 0.0;
        id = 0;
        genre = new ArrayList<>();
        country = "";
        sourceFrom = "";


        // Get UI
        ImageView imageView = getActivity().findViewById(R.id.movie_view_image);
        TextView textViewName = getActivity().findViewById(R.id.movie_view_name);
        TextView textViewDate = getActivity().findViewById(R.id.movie_view_release_year);
        TextView textViewSummary = getActivity().findViewById(R.id.movie_view_summary);
        TextView textViewCast = getActivity().findViewById(R.id.movie_view_cast);
        TextView textViewDirector = getActivity().findViewById(R.id.movie_view_director);
        TextView textViewGenre = getActivity().findViewById(R.id.movie_view_genre);
        TextView textViewCountry = getActivity().findViewById(R.id.movie_view_country);
        RatingBar ratingBar = getActivity().findViewById(R.id.movie_view_rating_bar);
        ImageDownload imageDownload = new ImageDownload(imageView);

        GetGenreCountryById getGenreCountryById = new GetGenreCountryById(textViewGenre,textViewCountry);
        GetCastDirectorById getCastDirectorById = new GetCastDirectorById(textViewCast,textViewDirector);

        // Set source
        sourceFrom = getArguments().getString("sourceFrom");
        if (sourceFrom == "MovieSearch") {
            // Get Info from movie search
            sharedPreferences = getActivity().getSharedPreferences("MessageFromSearch", Context.MODE_PRIVATE);
            name = sharedPreferences.getString("name",null);
            release = sharedPreferences.getString("release",null);
            imagePath = sharedPreferences.getString("imagePath",null);
            summary = sharedPreferences.getString("summary",null);
            score = Double.parseDouble(sharedPreferences.getString("score",null))*0.5;
            id = sharedPreferences.getInt("id",0);
            getGenreCountryById.execute(String.valueOf(id));
            getCastDirectorById.execute(String.valueOf(id));
            /*
            System.out.println(country);
            textViewCountry.setText("Country: "+country);
            String genreStr = "Genre: ";
            for (int i = 0; i < genre.size() && i < 3; i ++) {
                genreStr += genre.get(i);
            }
            System.out.println(genreStr);
            textViewGenre.setText(genreStr);

             */
        } else {

        }

        // Set UI
        System.out.println(imagePath);
        imageDownload.execute(imagePath);
        textViewName.setText(name);
        textViewDate.setText(release);
        textViewSummary.setText("Summary: " + summary);
        ratingBar.setRating(score.floatValue());

        Button buttonWatchlist = getActivity().findViewById(R.id.movie_view_watchlist);
        Button buttonMemoir = getActivity().findViewById(R.id.movie_view_memoir);

        buttonMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MessageFromMovieView", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name",name);
                editor.putString("release",release);
                editor.putString("imagePath",imagePath);
                editor.putInt("id",id);
                editor.apply();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_content_frame,new MovieMemoirAddFragment()).commit();
            }
        });

    }

    private class GetGenreCountryById extends AsyncTask<String, Void, String>{

        private TextView textViewGenre;
        private TextView textViewCountry;

        public GetGenreCountryById(TextView genreView, TextView countryView) {
            textViewGenre = genreView;
            textViewCountry = countryView;
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            return okHttpConnection.getGenreCountryById(id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textViewGenre.setText(setGenre(s));
            textViewCountry.setText(setCountry(s));
        }
    }

    // Set Genre
    protected String setGenre(String data) {
        String genreStr = "Genre: ";
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArrayGenre = jsonObject.getJSONArray("genres");

            for (int i = 0; i < jsonArrayGenre.length() && i < 3; i++) {
                JSONObject temp = jsonArrayGenre.getJSONObject(i);
                if (temp != null) {
                    genreStr = genreStr + temp.optString("name") +"/ ";
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return genreStr;
    }

    // Set Country
    protected String setCountry(String data) {
        String countryStr = "Country: ";
        try {
            JSONObject jsonObject = new JSONObject(data);

            JSONArray jsonArrayCountry = jsonObject.getJSONArray("production_countries");

            if (jsonArrayCountry.getJSONObject(0) != null) {
                countryStr = jsonArrayCountry.getJSONObject(0).optString("name");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countryStr;
    }

    private class GetCastDirectorById extends AsyncTask<String, Void, String> {
        private TextView textViewCast;
        private TextView textViewDirector;

        public GetCastDirectorById(TextView castView, TextView directorView) {
            textViewCast = castView;
            textViewDirector = directorView;
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            return okHttpConnection.getCastDirectorById(id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textViewCast.setText(setCast(s));
            textViewDirector.setText(setDirectory(s));
        }
    }

    protected String setCast(String data) {
        String castStr = "Cast: ";
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArrayCast = jsonObject.getJSONArray("cast");

            for (int i = 0; i < jsonArrayCast.length() && i < 3; i++) {
                JSONObject temp = jsonArrayCast.getJSONObject(i);
                if (temp != null) {
                    castStr = castStr + temp.optString("name") + "/ ";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return castStr;
    }

    protected String setDirectory(String data) {
        String directoryStr = "Directory: ";
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArrayDir = jsonObject.getJSONArray("crew");

            for (int i = 0; i < jsonArrayDir.length(); i++) {
                JSONObject temp = jsonArrayDir.getJSONObject(i);
                if (temp != null && temp.optString("job").equals("Director")) {
                    directoryStr = directoryStr + temp.optString("name") + "/ ";
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return directoryStr;
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
}
