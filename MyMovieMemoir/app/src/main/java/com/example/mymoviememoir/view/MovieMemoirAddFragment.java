package com.example.mymoviememoir.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.network.OKHttpConnection;

import java.util.ArrayList;

public class MovieMemoirAddFragment extends Fragment {

    OKHttpConnection okHttpConnection = null;
    SharedPreferences sharedPreferences = null;
    String name;
    String release;
    String imagePath;
    int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_movie_memoir_add,container,false);
        getActivity().setTitle("Add Movie Memoir");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        okHttpConnection = new OKHttpConnection();
        name = "";
        release = "";
        imagePath = "";
        id = 0;

        // Get Info from movie view
        sharedPreferences = getActivity().getSharedPreferences("MessageFromMovieView", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name",null);
        release = sharedPreferences.getString("release",null);
        imagePath = sharedPreferences.getString("imagePath",null);
        id = sharedPreferences.getInt("id",0);

        sendToast(name+" "+release+ " " + imagePath + " " + id);
    }

    // Toast
    protected void sendToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
