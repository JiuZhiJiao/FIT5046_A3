package com.example.mymoviememoir.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.Cinema;
import com.example.mymoviememoir.model.Credential;
import com.example.mymoviememoir.model.Person;
import com.example.mymoviememoir.network.OKHttpConnection;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieMemoirAddFragment extends Fragment {

    OKHttpConnection okHttpConnection = null;
    SharedPreferences sharedPreferences = null;
    Calendar calendar = null;
    Credential credential;
    Person person;
    int memoirId;
    int personId;
    String name;
    String release;
    String imagePath;
    String date;
    String time;
    String cinemaStr;
    int cinemaId;
    String cinemaName;
    String cinemaPostcode;
    String comment;
    Double ratingScore;
    Cinema[] cinemas;
    int id;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;

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
        date = "";
        time = "";
        cinemaStr = "";
        cinemaId = 0;
        cinemaName = "";
        cinemaPostcode = "";
        comment = "";
        ratingScore = 0.0;
        //cinemas = new Cinema[];
        id = 0;
        memoirId = 0;

        // Get All cinema
        GetAllCinema getAllCinema = new GetAllCinema();
        String cinemaFromNetBean = "";
        try {
            cinemaFromNetBean = getAllCinema.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        cinemas = gson.fromJson(cinemaFromNetBean, Cinema[].class);
        ArrayList<String> cinemaList = new ArrayList<>();
        for (Cinema cinema: cinemas) {
            String tempStr = cinema.getCinemapostcode() + "   " + cinema.getCinemaname();
            cinemaList.add(tempStr);
        }

        // Get Info from movie view
        sharedPreferences = getActivity().getSharedPreferences("MessageFromMovieView", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name",null);
        release = sharedPreferences.getString("release",null);
        imagePath = sharedPreferences.getString("imagePath",null);
        id = sharedPreferences.getInt("id",0);

        // Get UI
        TextView textViewName = getActivity().findViewById(R.id.memoir_add_title);
        TextView textViewRelease = getActivity().findViewById(R.id.memoir_add_relear);
        final TextView textViewDate = getActivity().findViewById(R.id.memoir_add_date);
        final TextView textViewTime = getActivity().findViewById(R.id.memoir_add_time);
        ImageView imageView = getActivity().findViewById(R.id.memoir_add_image);
        final EditText editTextComment = getActivity().findViewById(R.id.memoir_add_comment);
        final EditText editTextCinemaName = getActivity().findViewById(R.id.memoir_add_cinema_name);
        final EditText editTextCinemaPostcode = getActivity().findViewById(R.id.memoir_add_cinema_postcode);
        final Spinner spinner = getActivity().findViewById(R.id.memoir_add_spinner);
        final RatingBar ratingBar = getActivity().findViewById(R.id.memoir_add_rating_bar);
        Button buttonMemoir = getActivity().findViewById(R.id.memoir_add_btn_memoir);
        Button buttonCinema = getActivity().findViewById(R.id.memoir_add_btn_cinema);

        // Set UI
        textViewName.setText(name);
        textViewRelease.setText("Release Date: " + release);
        ImageDownload imageDownload = new ImageDownload(imageView);
        imageDownload.execute(imagePath);

        // Get date picker and time picker
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        if (month < 10 && dayOfMonth < 10) {
                            date = year + "-0" + month + "-0" + dayOfMonth;
                        } else if (month < 10 && dayOfMonth >= 10) {
                            date = year + "-0" + month + "-" + dayOfMonth;
                        } else if (month > 10 && dayOfMonth < 10) {
                            date = year + "-" + month + "-0" + dayOfMonth;
                        } else {
                            date = year + "-" + month + "-" + dayOfMonth;
                        }
                        textViewDate.setText(date);
                    }
                },mYear,mMonth,mDay).show();
            }
        });

        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = String.valueOf(hourOfDay)+":"+String.valueOf(minute)+":00";
                        textViewTime.setText(time);
                    }
                },mHour,mMinute,true).show();
            }
        });

        // set spinner
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, cinemaList);
        spinner.setAdapter(spinnerAdapter);
        // Get info from spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cinemaSelected = parent.getItemAtPosition(position).toString();
                String[] cinemaSelList = cinemaSelected.split("   ");
                cinemaId = position+1;
                cinemaPostcode = cinemaSelList[0];
                cinemaName = cinemaSelList[1];
                System.out.println(cinemaName);
                System.out.println(cinemaId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
            Gson gson1 = new Gson();
            Person[] people = gson1.fromJson(result, Person[].class);
            person = people[0];
        } else {
            credential = bundle.getParcelable("credentialFromSignUp");
            person = bundle.getParcelable("personFromSignUp");
        }
        personId = person.getPersonid();

        //Set new memoir id
        GetMemoirCount getMemoirCount = new GetMemoirCount();
        String countMemoir = "";
        try {
            countMemoir = getMemoirCount.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        memoirId = Integer.parseInt(countMemoir) + 1;

        buttonMemoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = editTextComment.getText().toString().trim();
                Float score = ratingBar.getRating();
                ratingScore = score.doubleValue();
                //System.out.println(memoirId+name+release+date+" "+time+".0"+comment+ratingScore+personId+cinemaId);
                String[] strings = setStrMemoir(setStrPerson(setStrCredential(setStrCinema())));
                /*
                Boolean checkEmpty = true;
                for (String s: strings) {
                    if (s.isEmpty()) {
                        checkEmpty = false;
                        break;
                    }
                }
                if (checkEmpty) {
                    new AddNewMemoir().execute(strings);
                    sendToast("Memoir Added");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_content_frame,new MovieMemoirFragment()).commit();
                } else {
                    sendToast("You Should Add All Information");
                }

                 */
                new AddNewMemoir().execute(strings);
                sendToast("Memoir Added");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_content_frame,new MovieMemoirFragment()).commit();
            }
        });

        buttonCinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cinemaName = editTextCinemaName.getText().toString().trim();
                String cinemaPostcode = editTextCinemaPostcode.getText().toString().trim();

                // Check exist in cinemas
                Boolean cinemaExisted = false;
                for (Cinema cinema: cinemas) {
                    if (cinema.getCinemaname().equals(cinemaName) && cinema.getCinemapostcode().equals(cinemaPostcode))
                        cinemaExisted = true;
                }

                if (!cinemaExisted) {
                    // Add cinema to NetBean
                    AddNewCinema addNewCinema = new AddNewCinema();
                    addNewCinema.execute(String.valueOf(cinemas.length+1),cinemaName,cinemaPostcode);
                    sendToast("Cinema Added");

                    // Update Spinner
                    String newCinema = cinemaPostcode + "   " + cinemaName;
                    spinnerAdapter.add(newCinema);
                    spinnerAdapter.notifyDataSetChanged();
                    spinner.setSelection(spinnerAdapter.getPosition(newCinema));
                } else {
                    sendToast("The Cinema Existed\nPlease Select The Cinema In The Spinner");
                }


            }
        });




    }

    private class AddNewMemoir extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return okHttpConnection.addMemoir(strings);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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

    private class AddNewCinema extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String response = "Cinema Added";
            return okHttpConnection.addCinema(strings) + response;
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

    private class GetMemoirCount extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return okHttpConnection.getCountMemoir();
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

    protected String[] setStrCinema() {
        String[] details = new String[] {Integer.toString(cinemaId),cinemaName,cinemaPostcode};
        return details;
    }

    protected String[] setStrCredential(String[] strings) {
        String[] details = new String[] {Integer.toString(credential.getCredentialsid()),credential.getPasswordhash(),credential.getSignupdate(),credential.getUsername()};
        return mergeStringArray(strings,details);
    }

    protected String[] setStrPerson(String[] strings) {
        String[] details = new String[] {Integer.toString(personId),person.getFirstname(),person.getSurname(),person.getGender(),person.getDob(),person.getAddress(),person.getState(),person.getPostcode()};
        return mergeStringArray(strings,details);
    }

    protected String[] setStrMemoir(String[] strings) {
        release += "T00:00:00+10:00";
        String watchedTime = date+"T"+time+"+10:00";
        String[] details = new String[] {Integer.toString(memoirId),name,release,watchedTime,comment,Double.toString(ratingScore)};
        return mergeStringArray(strings,details);
    }

    protected String[] mergeStringArray(String[] stringA, String[] stringB) {
        String[] strings = new String[stringA.length+stringB.length];
        for (int i = 0; i < stringA.length; i ++) {
            strings[i] = stringA[i];
        }
        for (int i = 0; i < stringB.length; i++) {
            strings[i+stringA.length] = stringB[i];
        }
        return strings;
    }
    // Toast
    protected void sendToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
