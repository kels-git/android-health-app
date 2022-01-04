package com.example.healthappdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CountriesAffected_DetailActivity extends AppCompatActivity {

    int countryPosition;

    TextView mShowCountry;
    TextView mTvTotalCases, mTvTotalActiveCases, mTvTotalRecovered, mTvTotalDeath;
    TextView mTvTodayConfirmCase, mTvTotalTest, mTvTodayTotalRecovered, mTvTodayDeath, mTvCritical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries_affected_detail);

        mShowCountry = findViewById(R.id.TV_CountryName);
        mTvTotalCases = findViewById(R.id.TV_Cases_DetailsConfirm);
        mTvTotalActiveCases = findViewById(R.id.TV_ActiveCases_Details);
        mTvTotalRecovered = findViewById(R.id.TV_RecoveredCases_Details);
        mTvTotalDeath = findViewById(R.id.TV_DeathCases_Details);


        mTvTodayConfirmCase = findViewById(R.id.TV_TodayCases_Details);
        mTvTotalTest = findViewById(R.id.TV_Test_Details);
        mTvTodayTotalRecovered = findViewById(R.id.TV_TodayRecovered_Details);
        mTvTodayDeath = findViewById(R.id.TV_TodayDeath_Details);
        mTvCritical = findViewById(R.id.TV_TodayCritical_Details);


        Intent i = getIntent();
        countryPosition = i.getIntExtra("position", 0);

         mShowCountry.setText(CountriesAffectedActivity.mCountryModel_List.get(countryPosition).getCountry());
        mTvTotalCases.setText(CountriesAffectedActivity.mCountryModel_List.get(countryPosition).getCases());
        mTvTotalActiveCases.setText(CountriesAffectedActivity.mCountryModel_List.get(countryPosition).getActive());
        mTvTotalRecovered.setText(CountriesAffectedActivity.mCountryModel_List.get(countryPosition).getRecovered());
        mTvTotalDeath.setText(CountriesAffectedActivity.mCountryModel_List.get(countryPosition).getDeaths());

        mTvTodayConfirmCase.setText(CountriesAffectedActivity.mCountryModel_List.get(countryPosition).getTodayCases());
        mTvTotalTest.setText(CountriesAffectedActivity.mCountryModel_List.get(countryPosition).getTests());
        mTvTodayTotalRecovered.setText(CountriesAffectedActivity.mCountryModel_List.get(countryPosition).getTodayRecovered());
        mTvTodayDeath.setText(CountriesAffectedActivity.mCountryModel_List.get(countryPosition).getTodayDeaths());
        mTvCritical.setText(CountriesAffectedActivity.mCountryModel_List.get(countryPosition).getCritical());

    }
}