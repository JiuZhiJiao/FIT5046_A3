package com.example.mymoviememoir.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.database.WatchlistDatabase;
import com.example.mymoviememoir.entity.Watchlist;
import com.example.mymoviememoir.viewmodel.WatchlistViewModel;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.ArrayList;
import java.util.List;


public class WatchlistFragment extends Fragment {

    WatchlistDatabase db = null;
    WatchlistViewModel watchlistViewModel;
    SharedPreferences sharedPreferences;
    String name;
    String release;
    String current;
    int id;
    Watchlist watchlist;
    List<Watchlist> watchlistList;

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

        id = 0;
        name = "";
        release = "";
        current = "";
        watchlist = new Watchlist(name,release,current);
        watchlistList = new ArrayList<>();
        boolean checkExist = false;

        final ListView listView = getActivity().findViewById(R.id.watchlist_list_view);
        sharedPreferences = getActivity().getSharedPreferences("MessageFromMovieViewByWatchlist", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name",null);
        release = sharedPreferences.getString("release", null);
        current = sharedPreferences.getString("current",null);
        id = sharedPreferences.getInt("id",0);
        watchlist = new Watchlist(name,release,current);

        // View Model
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initializeVars(getActivity().getApplication());
        watchlistViewModel.getAllWatchlist().observe(this, new Observer<List<Watchlist>>() {
            @Override
            public void onChanged(List<Watchlist> watchlists) {
                watchlistList = watchlists;
                MyAdapter myAdapter = new MyAdapter(getActivity(),watchlists,listView);
                listView.setAdapter(myAdapter);
            }
        });

        // Add New Watchlist
        /*
        if (watchlistViewModel.getAllWatchlist().getValue() != null) {
            for (Watchlist w: watchlistViewModel.getAllWatchlist().getValue()) {
                if (w.getUid() == id)
                    checkExist = true;
            }
        }
        if (!checkExist)

         */
        /*
        for (Watchlist w: watchlistList) {
            if (!w.getMovieName().equals(name))
                watchlistViewModel.insert(watchlist);
        }

         */

        watchlistViewModel.insert(watchlist);


        //System.out.println(watchlistList.get(0).getMovieName());

        final int[] selectedPosition = {0};
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                System.out.println(watchlistList.toString());
                id = watchlistList.get(position).getUid();
                name = watchlistList.get(position).getMovieName();
                release = watchlistList.get(position).getReleaseDate();
                current = watchlistList.get(position).getAddedDate();
                setWatchlist((int) id,name,release,current);
                selectedPosition[0] = watchlistList.get(position).getUid();
                sendToast("You Select: "+name);
            }
        });


        Button buttonView = getActivity().findViewById(R.id.watchlist_view);
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MessageFromWatchlist", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name",watchlist.getMovieName());
                editor.apply();

                /*
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
                 */

                // This bundle let Movie View Screen to check which fragment from
                MovieViewFragment movieViewFragment = new MovieViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("sourceFrom","Watchlist");
                movieViewFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_content_frame,movieViewFragment).commit();
            }
        });

        Button buttonDelete = getActivity().findViewById(R.id.watchlist_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("Confirm Delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        watchlistViewModel.delete(watchlist);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNeutralButton("Delete All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        watchlistViewModel.deleteAll();
                    }
                }).create();
                alertDialog.show();
            }
        });

        /*
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
                        bundle.putString("sourceFrom","Watchlist");
                        movieViewFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_content_frame,movieViewFragment).commit();
                      }
                });
         */
    }

    protected void setWatchlist(int id, String name, String release, String current) {
        watchlist = new Watchlist(id,name,release,current);
    }

    public class MyAdapter extends BaseAdapter {

        List<Watchlist> list;
        Context context;
        LayoutInflater inflater;
        ListView listView;

        public MyAdapter(Context context, List<Watchlist> list, ListView listView) {
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
            holder = new ViewHolder();
            /*
            if (convertView == null) {
                holder = new ViewHolder();

             */
            convertView = inflater.inflate(R.layout.screen_watchlist_listview, parent, false);
            holder.textViewTitle = convertView.findViewById(R.id.watchlist_lv_title);
            holder.textViewRelease = convertView.findViewById(R.id.watchlist_lv_release);
            holder.textViewAdded = convertView.findViewById(R.id.watchlist_lv_added_date);
                /*
                convertView.setTag(holder+"Message");
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

                 */

            holder.textViewTitle.setText(list.get(position).getMovieName());
            holder.textViewRelease.setText("Release Date: "+list.get(position).getReleaseDate());
            holder.textViewAdded.setText("Added At "+list.get(position).getAddedDate());


            return convertView;
        }
    }

    class ViewHolder {
        TextView textViewTitle;
        TextView textViewRelease;
        TextView textViewAdded;
    }

    // Toast
    protected void sendToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }









}
