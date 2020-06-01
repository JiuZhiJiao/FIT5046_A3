package com.example.mymoviememoir.view;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.Cinema;
import com.example.mymoviememoir.model.Credential;
import com.example.mymoviememoir.model.Person;
import com.example.mymoviememoir.network.OKHttpConnection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    OKHttpConnection okHttpConnection;
    String cinemaStr;
    Credential credential;
    Person person;
    Cinema[] cinemas;
    List<LatLng> cinemaLocation;
    LatLng latLng;
    List<Address> addressTemp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_map,container,false);
        getActivity().setTitle("Map");

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        okHttpConnection = new OKHttpConnection();
        //GetAllCinema getAllCinema = new GetAllCinema();

        // Get All cinema
        cinemaStr = "";
        try {
            cinemaStr = new GetAllCinema().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        cinemas = gson.fromJson(cinemaStr, Cinema[].class);

        // Get Personal Info
        credential = new Credential();
        person = new Person();
        FindByCredentialId findByCredentialId = new FindByCredentialId();
        updateInfo(findByCredentialId);

        // Show Map
        mapView = getActivity().findViewById(R.id.map_google);
        mapView.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS != errorCode) {
            GooglePlayServicesUtil.getErrorDialog(errorCode,getActivity(),0).show();
        } else {
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap onMap) {
        googleMap = onMap;

        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(),Locale.ENGLISH);
        // Set User Home
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(person.getAddress(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LatLng userHome = new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
        googleMap.addMarker(new MarkerOptions().position(userHome).title((person.getFirstname())+"'s Home").icon(BitmapDescriptorFactory.defaultMarker(180)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userHome,10));
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Set Cinema Location
        for (int i = 0; i < cinemas.length; i++) {
            String searchStr = cinemas[i].getCinemapostcode() + " " + cinemas[i].getCinemaname();
            try {
                addressTemp = geocoder.getFromLocationName(searchStr,1);
                if (addressTemp != null && addressTemp.size()>0) {
                    Double lat = addressTemp.get(0).getLatitude();
                    Double lon = addressTemp.get(0).getLongitude();
                    latLng = new LatLng(lat,lon);
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(cinemas[i].getCinemaname()));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetAllCinema extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return okHttpConnection.getAllCinema();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    // update current person and credential info
    public void updateInfo(FindByCredentialId findByCredentialId) {
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

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
