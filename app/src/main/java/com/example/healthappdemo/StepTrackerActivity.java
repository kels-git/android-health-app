package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StepTrackerActivity extends AppCompatActivity {

    TextView mTV_StepCounter, mTV_StepCounter_meter, mCalories_Burn, mCounter_Duration,
            TargetSteps, mBtReset_Counter, mSaveDataToFireStore;
    SensorManager sensorManager;
    Sensor sensor;
    private Boolean isCounterSensorPresent;
    int stepCount = 0;
    double DeltaMagnitude;
    private double PreviousMagnitude = 0;
    float totalDistance;
    float totalCaloriesBurned, totalDuration, hours, minutes, seconds;
    LottieAnimationView mHealthLottie;
    String USER_ID, STEP_COUNTER;
    BarChart mBarChart;
    String saveCurrentTime, saveCurrentDate, saveCurrentDay;

    final static float DEFAULT_STEP_SIZE = Locale.getDefault() == Locale.TAIWAN ? 2.5f : 75f;
    final static String DEFAULT_STEP_UNIT = Locale.getDefault() == Locale.TAIWAN ? "ft" : "cm";
    final static int DEFAULT_GOAL = 10000;
    final boolean showSteps = true;
    int goal = DEFAULT_GOAL;
    float distance, stepsize = DEFAULT_STEP_SIZE;
    String[] colors = {"#282A80", "#0C6C59", "#21A8C4", "#D5DC56", "#2B417A", "#96DC56", "#569CDC"};

    String validateDateCheck = "^.*?(Sat|Mon|Tue|Wed|Thu|Sun).*$";
    boolean stepsize_cm = true;

    BarModel bm;
    DrawerLayout mDrawerLayout;
    ImageView btMenu, btProfile, btSetting;
    MainNavAdapter.onItemClickListener mListener;
    RecyclerView mRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_tracker);

        mHealthLottie = findViewById(R.id.health_loading_lottie);

        //mBtReset_Counter = findViewById(R.id.TV_StepCounter_reset);
        mCalories_Burn = findViewById(R.id.TV_StepCounter_Calories);
        mCounter_Duration = findViewById(R.id.TV_StepCounter_duration);
        mTV_StepCounter_meter = findViewById(R.id.TV_StepCounter_meter);
        mTV_StepCounter = findViewById(R.id.TV_StepCounter);
        mBtReset_Counter = findViewById(R.id.TV_StepCounter_display_reset);

        TargetSteps = findViewById(R.id.TV_StepCounter_target_goal);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);


        mBarChart = (BarChart) findViewById(R.id.barchart);

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
                Intent intent = new Intent(StepTrackerActivity.this, UserProfileActivity.class);
                startActivity(intent); // start  activity
                finish(); // destroy older activity
            }
        });

        //set onclick to navigate setting activity
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StepTrackerActivity.this, UserSettingsActivity.class);
                startActivity(intent); // start  activity
                finish(); // destroy older activity
            }
        });

        getStepCount();
        _saveToDbSteps();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getStepCount() {

        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            FirebaseAuth.getInstance().getCurrentUser().getUid();

            SensorEventListener stepDetector = new SensorEventListener() {
                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    if (sensorEvent != null) {
                        float x_accelerator = sensorEvent.values[0];
                        float y_accelerator = sensorEvent.values[1];
                        float z_accelerator = sensorEvent.values[2];

                        double Magnitude = Math.sqrt(x_accelerator * x_accelerator + y_accelerator * y_accelerator + z_accelerator * z_accelerator);
                        DeltaMagnitude = Magnitude - PreviousMagnitude;
                        PreviousMagnitude = Magnitude;

                        if (DeltaMagnitude > 1.5) {
                            stepCount++;

                            //Calculating total distance traveled (convert in M(1 FOOT - M))
                            totalDistance = stepCount * 0.3048f;

                            //Calculating total calories burned(convert 0.1 FT to M value multiply by steps)
                            totalCaloriesBurned = stepCount * 0.03048f;

                            //Calculating total duration (1 ft - to meeter)
                            totalDuration = stepCount * 0.3048f;

                            hours = totalDuration / 3600;
                            minutes = (totalDuration % 3600) / 60;
                            seconds = totalDuration % 60;

                        }


                        mTV_StepCounter.setText(String.valueOf(stepCount));
                        mTV_StepCounter_meter.setText(String.format("%.2f", totalDistance));
                        mCalories_Burn.setText(String.format("%.0f", totalCaloriesBurned));
                        //mCounter_Duration.setText(String.format("%.0f",hours)  +  String.format("%.0f",minutes)  +  String.format("%.0f",seconds));
                        mCounter_Duration.setText(String.format("%.0f", minutes));
                        TargetSteps.setText(String.valueOf(stepCount) + "/" + "10,000 steps");



                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                }
            };
            sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL, 0);

            updateBarChart();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateBarChart() {

        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        STEP_COUNTER = FirebaseAuth.getInstance().getCurrentUser().getUid();

        CollectionReference colRef = FirebaseFirestore.getInstance()
                .collection("REGISTERED USERS").document(USER_ID)
                .collection("STEP COUNTER");


        Query q1 = colRef.orderBy("mStepDate", Query.Direction.ASCENDING).limitToLast(6);

        q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    int colornumb = 0;

                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        String getFireStoreDay;
                        getFireStoreDay = ds.getString("mStepDay");

                        int stepCount1 = Integer.valueOf(ds.getString("mStepCount"));

                        if (colornumb >= 7) {
                            colornumb = 0;
                        }
                        String color = colors[colornumb];
                        colornumb++;

                        if (stepCount1 > 0) {
                            bm = new BarModel(getFireStoreDay, 0, stepCount1 > goal ? Color.parseColor(color) : Color.parseColor(color));
                            if (showSteps) {
                                bm.setValue(stepCount1);
                            } else {
                                distance = stepCount1 * stepsize;
                                if (stepsize_cm) {
                                    distance /= 100000;
                                } else {
                                    distance /= 5280;
                                }
                                distance = Math.round(distance * 1000) / 1000f; // 3 decimals
                                bm.setValue(distance);
                            }
                            mBarChart.addBar(bm);
                        }


                    }

                    if (mBarChart.getData().size() > 0) {
                        mBarChart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                //Dialog_Statistics.getDialog(this, since_boot).show();
                            }
                        });

                        mBarChart.startAnimation();
                    } else {
                        mBarChart.setVisibility(View.GONE);
                    }
                }


            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void _saveToDbSteps() {

        String stepCountNum = mTV_StepCounter.getText().toString();
        String stepCountMeter = mTV_StepCounter_meter.getText().toString();
        String stepCalories = mCalories_Burn.getText().toString();
        String stepDuration = mCounter_Duration.getText().toString();

        LocalDate todayStepsCountDate = LocalDate.now();
        String countNowSteps = todayStepsCountDate.format(DateTimeFormatter.ISO_DATE);

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDay = new SimpleDateFormat("E");
        saveCurrentDay = currentDay.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            STEP_COUNTER = FirebaseAuth.getInstance().getCurrentUser().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("STEP COUNTER");

            LocalDate getToday = LocalDate.now();
            String convertedTodayToString = getToday.format(DateTimeFormatter.ISO_DATE);


            Query q1 = colRef.whereEqualTo("mStepDate", convertedTodayToString);
            q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    boolean isExisting = false;
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        String getFireStoreDate;
                        getFireStoreDate = ds.getString("mStepDate");

                        assert getFireStoreDate != null;
                        if (getFireStoreDate.equals(convertedTodayToString)) {
                            isExisting = true;

                            Toast.makeText(StepTrackerActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();

                            StepCounter_Model stepCountUpdate = new StepCounter_Model(countNowSteps, saveCurrentDay, saveCurrentTime, stepCountNum,
                                    stepCountMeter, stepCalories, stepDuration);
                            StepCounter_Model StepCountData = ds.toObject(StepCounter_Model.class);
                            assert StepCountData != null;
                            StepCountData.setmStepID(ds.getId());
                            String documentID = StepCountData.getmStepID();
                            Map<String, Object> saveInfo = new HashMap<>();

                            saveInfo.put("mStepDate", stepCountUpdate.mStepDate);
                            saveInfo.put("mStepDay", stepCountUpdate.mStepDay);
                            saveInfo.put("mStepTime", stepCountUpdate.mStepTime);
                            saveInfo.put("mStepCount", stepCountUpdate.mStepCount);
                            saveInfo.put("mStepCountMeter", stepCountUpdate.mStepCountMeter);
                            saveInfo.put("mStepCalories", stepCountUpdate.mStepCalories);
                            saveInfo.put("mStepDuration", stepCountUpdate.mStepDuration);

                            colRef.document(documentID).update(saveInfo);

                        }
                    }
                    if (!isExisting) {

                        resetCounter();
                        addTo_FireStoreDB(countNowSteps, saveCurrentDay, saveCurrentTime, stepCountNum,
                                stepCountMeter, stepCalories, stepDuration);
                    }
                }
            });


        }


    }


    private void addTo_FireStoreDB(String countNowSteps, String saveCurrentDay, String saveCurrentTime,
                                   String stepCountNum, String stepCountMeter, String stepCalories, String stepDuration) {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            STEP_COUNTER = FirebaseAuth.getInstance().getCurrentUser().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("STEP COUNTER");
            StepCounter_Model stepCountData = new StepCounter_Model(countNowSteps, saveCurrentDay, saveCurrentTime, stepCountNum,
                    stepCountMeter, stepCalories, stepDuration);

            colRef.add(stepCountData).addOnSuccessListener(documentReference -> {
                assert documentReference != null;
                Toast.makeText(StepTrackerActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void resetCounter() {

                    FirebaseFirestore.getInstance()
                            .collection("REGISTERED USERS").document(USER_ID)
                            .collection("STEP COUNTER");
                    stepCount = 0;
                    //Calculating total distance traveled (convert in M(1 FOOT - M))
                    totalDistance = stepCount * 0.3048f;

                    //Calculating total calories burned(convert 0.1 FT to M value multiply by steps)
                    totalCaloriesBurned = stepCount * 0.03048f;

                    //Calculating total duration
                    totalDuration = stepCount * 0.3048f;

                    //Calculating total duration
                    totalDuration = stepCount * 0.3048f;
                    hours = totalDuration * 3600;
                    minutes = totalDuration * 60;

                    mTV_StepCounter.setText(String.valueOf(stepCount));
                    mTV_StepCounter_meter.setText(String.valueOf(totalDistance));
                    mCalories_Burn.setText(String.format("%.0f", totalCaloriesBurned));
                    mCounter_Duration.setText(String.format("%.0f", minutes));

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();

        NewsFeedActivity.closeDrawer(mDrawerLayout);

        _saveToDbSteps();

        SharedPreferences preferences = StepTrackerActivity.this.getSharedPreferences("PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putInt("StepCount", stepCount);
        editor.commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = StepTrackerActivity.this.getSharedPreferences("PREF", MODE_PRIVATE);
        stepCount = preferences.getInt("StepCount", 0);





    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NewsFeedActivity.closeDrawer(mDrawerLayout);

    }
}