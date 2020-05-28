package com.example.mymoviememoir.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class MovieSearchFragment extends Fragment {

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

        /*
        // get data

        // Set list view
        String[] colHead = new String[] {"image","moviename","releaseyear"};
        int[] dataCell = new int[] {R.id.movie_search_image,R.id.movie_search_tv_name,R.id.movie_search_tv_date};
        ListView listView = getActivity().findViewById(R.id.movie_search_list_view);
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), null, R.layout.screen_movie_search_listview,colHead,dataCell);
        listView.setAdapter(simpleAdapter);

         */
    }

    // Get image from url
    public Bitmap getImage(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            ResponseBody body = client.newCall(request).execute().body();
            InputStream inputStream = body.byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
