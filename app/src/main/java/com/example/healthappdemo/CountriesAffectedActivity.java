package com.example.healthappdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountriesAffectedActivity extends AppCompatActivity {

    EditText mSearchCountries;
    ListView mListView;
    public static List<CountryModel> mCountryModel_List = new ArrayList<>();
    CountryModel countryModel;
    CountriesAffected_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries_affected);

        mSearchCountries = findViewById(R.id.et_SearchAffected_Countries);
        mListView = findViewById(R.id.ListView_Search_Affected_Countries);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                startActivity(new Intent(getApplicationContext(), CountriesAffected_DetailActivity.class).putExtra("position", position));
            }
        });

        getAffectedCountries();

        mSearchCountries.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                       mAdapter.getFilter().filter(charSequence);
                       mAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getAffectedCountries() {

        String url = "https://corona.lmao.ninja/v2/countries/";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray JsonArray = new JSONArray(response);
                            for(int i = 0; i<JsonArray.length(); i++){

                                JSONObject jsonObject = JsonArray.getJSONObject(i);

                                String countryName = jsonObject.getString("country");
                                String cases = jsonObject.getString("cases");
                                String todayCases = jsonObject.getString("todayCases");
                                String Deaths = jsonObject.getString("deaths");
                                String todayDeaths = jsonObject.getString("todayDeaths");
                                String recovered = jsonObject.getString("recovered");
                                String todayRecovered = jsonObject.getString("todayRecovered");
                                String active = jsonObject.getString("active");
                                String critical = jsonObject.getString("critical");
                                String tests = jsonObject.getString("tests");

                                JSONObject object = jsonObject.getJSONObject("countryInfo");
                                String flagUrl = object.getString("flag");

                              countryModel = new CountryModel(flagUrl,countryName, cases, todayCases, Deaths,
                                      todayDeaths, recovered,todayRecovered, active, critical, tests );
                                mCountryModel_List.add(countryModel);
                            }

                            mAdapter = new CountriesAffected_Adapter(CountriesAffectedActivity.this, mCountryModel_List);
                            mListView.setAdapter(mAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CountriesAffectedActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}