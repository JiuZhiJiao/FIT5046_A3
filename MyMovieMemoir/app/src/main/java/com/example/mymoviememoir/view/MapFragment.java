package com.example.mymoviememoir.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.Credential;

public class MapFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_map,container,false);
        getActivity().setTitle("Map");
        return view;
    }

    Credential credential;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get info from sign in
        Bundle bundle = getActivity().getIntent().getExtras();
        credential = bundle.getParcelable("credentialFromSignIn");
        TextView textView = getActivity().findViewById(R.id.map_tv);
        textView.setText(credential.getPasswordhash());
    }
}
