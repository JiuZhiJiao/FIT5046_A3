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
import com.example.mymoviememoir.model.Person;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_home, container, false);
        getActivity().setTitle("Home");
        return view;
    }

    Credential credential;
    Person person;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get info from sign in and sign up
        Bundle bundle = getActivity().getIntent().getExtras();
        String password = "";

        if (bundle.getParcelable("credentialFromSignIn") != null) {
            credential = bundle.getParcelable("credentialFromSignIn");
            password = credential.getPasswordhash();
        } else {
            credential = bundle.getParcelable("credentialFromSignUp");
            person = bundle.getParcelable("personFromSignUp");
            password = person.getFirstname();
        }

        TextView textView = getActivity().findViewById(R.id.home_tv_message);
        textView.setText(password);

        // pass bundle between fragments
    }
}
