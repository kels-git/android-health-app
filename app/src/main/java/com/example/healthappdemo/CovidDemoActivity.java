package com.example.healthappdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CovidDemoActivity extends AppCompatActivity {

    LinearLayout mBtn_TrackCountries;
    TextView mTimeUpdated, mCountryDropDown;
    TextView mTotalConfirm, mTotalActiveCases, mTotalRecovered, mTotalDeath, mCountryAffected;
    TextView mTodayConfirmCase, mTotalTest, mTodayTotalRecovered, mTodayDeath;
    PieChart mPieChart;

    List<CountryModel> list;

    DrawerLayout mDrawerLayout;
    ImageView btMenu, btProfile, btSetting;
    MainNavAdapter.onItemClickListener mListener;
    RecyclerView mRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_demo);

        mTimeUpdated = findViewById(R.id.TV_time_Updated);
        mCountryDropDown = findViewById(R.id.TV_Country_drop_down);

        mTotalConfirm = findViewById(R.id.TV_Total_Confirmed_Cases);
        mTotalActiveCases = findViewById(R.id.TV_Total_Active_Cases);
        mTotalRecovered = findViewById(R.id.TV_Total_Recovered_Cases);
        mTotalDeath = findViewById(R.id.TV_Total_ReportedDeath_Cases);
        mTotalTest = findViewById(R.id.TV_Total_test);
        mCountryAffected = findViewById(R.id.TV_Total_Countries_affected);

        mTodayConfirmCase = findViewById(R.id.TV_TodayConfirm_Cases);
        mTodayTotalRecovered = findViewById(R.id.TV_TodayRecovered_Cases);
        mTodayDeath = findViewById(R.id.TV_Reported_DeathToday_Cases);
        mBtn_TrackCountries = findViewById(R.id.LinearLayout_Track_Countries);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);


        mPieChart = findViewById(R.id.pieChart);


        //set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set the adapter for drawers
        mRecyclerView.setAdapter(new MainNavAdapter(NewsFeedActivity.drawerArrayList, mListener, this));

        //set onclick to open drawer menu
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open drawer
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //set onclick to navigate profile activity
        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CovidDemoActivity.this, UserProfileActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity


            }
        });

        //set onclick to navigate setting activity
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CovidDemoActivity.this, UserSettingsActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity


            }
        });

        list = new ArrayList<>();


        fetchData();

        trackCountries();




    }

    private void fetchData() {

        String url = "https://corona.lmao.ninja/v2/all/";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            mTotalConfirm.setText(jsonObject.getString("cases"));
                            mTotalActiveCases.setText(jsonObject.getString("active"));
                            mTotalRecovered.setText(jsonObject.getString("recovered"));
                            mTotalDeath.setText(jsonObject.getString("deaths"));
                            mCountryAffected.setText(jsonObject.getString("affectedCountries"));

                            mTodayDeath.setText(jsonObject.getString("todayDeaths"));
                            mTodayConfirmCase.setText(jsonObject.getString("todayCases"));
                            mTodayTotalRecovered.setText(jsonObject.getString("todayRecovered"));
                            mTotalTest.setText(jsonObject.getString("tests"));


                            mPieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(mTotalConfirm.getText().toString()), Color.parseColor("#FFE600")));
                            mPieChart.addPieSlice(new PieModel("Active",Integer.parseInt(mTotalActiveCases.getText().toString()), Color.parseColor("#00AEEF")));
                            mPieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt(mTotalRecovered.getText().toString()), Color.parseColor("#00AE00")));
                            mPieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(mTotalDeath.getText().toString()), Color.parseColor("#FF0000")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CovidDemoActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void trackCountries() {
        mBtn_TrackCountries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CountriesAffectedActivity.class));
            }
        });
    }


//    private void setText(String upDated) {
//        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
//        long millisecond = Long.parseLong(upDated);
//        Calendar calender = Calendar.getInstance();
//        calender.setTimeInMillis(millisecond);
//        mTimeUpdated.setText("Updated at "+ dateFormat.format(calender.getTime()));
//
//    }


    private void setData_Display(){

//        ApiUtilities.getApiInterface().getCountryData()
//                .enqueue(new Callback<List<CountryModel>>() {
//                    @Override
//                    public void onResponse(Call<List<CountryModel>> call, Response<List<CountryModel>> response) {
//
//                        list.addAll(response.body());
//
//                        for(int i = 0; i< list.size(); i++){
//                            if(list.get(i).getCountry().equals("malaysia")){
//                                int confirm = Integer.parseInt(list.get(i).getCases());
//                                int active = Integer.parseInt(list.get(i).getActive());
//                                int recovered = Integer.parseInt(list.get(i).getRecovered());
//                                int death = Integer.parseInt(list.get(i).getDeaths());
//
//                                mTotalConfirm.setText(NumberFormat.getInstance().format(confirm));
//                                mTotalActiveCases.setText(NumberFormat.getInstance().format(active));
//                                mTotalRecovered.setText(NumberFormat.getInstance().format(recovered));
//                                mTodayDeath.setText(NumberFormat.getInstance().format(death));
//
//                                mTodayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
//                                mTodayConfirmCase.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
//                                mTodayTotalRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
//                                mTotalTest.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));
//
//                                setText(list.get(i).getUpDated());
//
//                                mPieChart.addPieSlice(new PieModel("Confirm",confirm , getResources().getColor(R.color.highlight)));
//                                mPieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.blue)));
//                                mPieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.positive)));
//                                mPieChart.addPieSlice(new PieModel("Death", death, getResources().getColor(R.color.redSquare)));
//
//
//
//                            }
//
//                        }
//
//                        mPieChart.startAnimation();
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<CountryModel>> call, Throwable t) {
//                        Toast.makeText(CovidDemoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    @Override
    protected void onPause() {
        super.onPause();

        NewsFeedActivity.closeDrawer(mDrawerLayout);
    }
}