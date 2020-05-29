package com.example.mymoviememoir.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;


public class WatchlistFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_watchlist, container, false);
        getActivity().setTitle("Watchlist");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Send Twitter
        Twitter.initialize(getActivity());
        Button buttonShare = getActivity().findViewById(R.id.watchilist_bt_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToTwitter(v);
            }
        });
    }

    public void shareToTwitter(View view) {
        // Set Twitter Content
        TweetComposer.Builder builder = new TweetComposer.Builder(getActivity()).text("Twitter from mymoviememoir");
        builder.show();
    }
}
