package com.example.mymoviememoir.view;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.model.Credential;
import com.example.mymoviememoir.model.Person;
import com.example.mymoviememoir.network.OKHttpConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ReportFragment extends Fragment {

    OKHttpConnection okHttpConnection = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_report, container, false);
        getActivity().setTitle("Report");
        return view;
    }

    Credential credential;
    Person person;
    String personId;
    String year;
    String startDate;
    String endDate;


    Calendar calendar;
    int mYear;
    int mMonth;
    int mDay;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        okHttpConnection = new OKHttpConnection();
        credential = new Credential();
        person = new Person();

        FindByCredentialId findByCredentialId = new FindByCredentialId();
        updateInfo(findByCredentialId);
        personId = Integer.toString(person.getPersonid());

        // get input from UI
        final TextView textViewStart = getActivity().findViewById(R.id.report_tv_start);
        final TextView textViewEnd = getActivity().findViewById(R.id.report_tv_end);


        startDate = "2000-01-01";
        textViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mYear = year;
                                mDay = dayOfMonth;
                                mMonth = month;
                                month = month + 1;
                                if (month < 10 && dayOfMonth < 10) {
                                    startDate = year + "-0" + month + "-0" + dayOfMonth;
                                } else if (month < 10 && dayOfMonth >= 10) {
                                    startDate = year + "-0" + month + "-" + dayOfMonth;
                                } else if (month > 10 && dayOfMonth < 10) {
                                    startDate = year + "-" + month + "-0" + dayOfMonth;
                                } else {
                                    startDate = year + "-" + month + "-" + dayOfMonth;
                                }
                                textViewStart.setText(startDate);
                            }
                        }, mYear,mMonth,mDay);
                dialog.show();
            }
        });

        endDate = "2020-01-01";
        textViewEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mYear = year;
                                mDay = dayOfMonth;
                                mMonth = month;
                                month = month + 1;
                                if (month < 10 && dayOfMonth < 10) {
                                    endDate = year + "-0" + month + "-0" + dayOfMonth;
                                } else if (month < 10 && dayOfMonth >= 10) {
                                    endDate = year + "-0" + month + "-" + dayOfMonth;
                                } else if (month > 10 && dayOfMonth < 10) {
                                    endDate = year + "-" + month + "-0" + dayOfMonth;
                                } else {
                                    endDate = year + "-" + month + "-" + dayOfMonth;
                                }
                                textViewEnd.setText(endDate);
                            }
                        }, mYear,mMonth,mDay);
                dialog.show();
            }
        });




        // Get year from spinner
        year = "";
        final Spinner spinner = getActivity().findViewById(R.id.report_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        // generate pie chart
        final PieChart pieChart = getActivity().findViewById(R.id.report_pie_chart);
        Button buttonPieChart = getActivity().findViewById(R.id.report_bt_pie);
        buttonPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = startDate;
                String end = endDate;
                start += " 00:00:00";
                end += " 00:00:00";

                String data = "";
                TotalNumberByPostcode totalNumberByPostcode = new TotalNumberByPostcode();
                try {
                    data = totalNumberByPostcode.execute(personId,start,end).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String,String>>>(){}.getType();
                List<Map<String,String>> dataList = gson.fromJson(data,type);

                for (Map<String,String> m : dataList) {
                    System.out.println(m.toString());
                }

                List<PieEntry> entries = new ArrayList<>();
                for (Map<String,String> m : dataList) {
                    entries.add(new PieEntry(Float.parseFloat(m.get("totalnumber")),m.get("postcode")));
                }
                PieDataSet pieDataSet = new PieDataSet(entries,"PostCode");
                // set color
                pieDataSet.setColors(Color.parseColor("#feb64d"),Color.parseColor("#ff7c7c"), Color.parseColor("#9287e7"), Color.parseColor("#60acfc"));

                PieData pieData = new PieData(pieDataSet);
                // change to percentage
                pieData.setDrawValues(true);
                pieData.setValueFormatter(new PercentFormatter());
                pieData.setValueTextSize(12f);

                pieChart.setData(pieData);
                pieChart.invalidate();
                pieChart.setUsePercentValues(true);
                // change format
                pieChart.setHoleRadius(0f);
                pieChart.setTransparentCircleAlpha(0);
                // remove label
                Description description = new Description();
                description.setText("Pie Chart, shows with %");
                pieChart.setDescription(description);
                pieChart.setDrawEntryLabels(true);

            }
        });



        // generate bar chart
        final BarChart barChart = getActivity().findViewById(R.id.report_bar_chart);
        Button buttonBar = getActivity().findViewById(R.id.report_bt_bar);
        buttonBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "";
                TotalNumberPerMon totalNumberPerMon = new TotalNumberPerMon();
                try {
                    data = totalNumberPerMon.execute(personId,year).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String,String>>>(){}.getType();
                List<Map<String,String>> dataList = gson.fromJson(data,type);

                for (Map<String,String> m : dataList) {
                    System.out.println(m.toString());
                }

                List<BarEntry> entries = new ArrayList<>();
                for (Map<String,String> m : dataList) {
                    entries.add(new BarEntry(Integer.parseInt(m.get("month")),Integer.parseInt(m.get("totalnumber"))));
                }

                BarDataSet barDataSet = new BarDataSet(entries,"Month");
                barDataSet.setColors(Color.parseColor("#feb64d"),Color.parseColor("#ff7c7c"), Color.parseColor("#9287e7"), Color.parseColor("#60acfc"));

                BarData barData = new BarData(barDataSet);

                barData.setBarWidth(0.5f);

                barChart.setData(barData);

                // change description
                Description description = new Description();
                description.setText("Bar Chart");
                barChart.setDescription(description);

                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

                barChart.invalidate();

            }
        });








        System.out.println(credential.getUsername());
        System.out.println(person.getFirstname());

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

    // get total number by postcode
    private class TotalNumberByPostcode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String start = strings[1];
            String end = strings[2];
            return okHttpConnection.totalNumberByPostcode(id,start,end);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    // get total number per month in one year
    private class TotalNumberPerMon extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String year = strings[1];
            return okHttpConnection.totalNumberByWatchedMonth(id,year);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    // Toast
    protected void sendToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
