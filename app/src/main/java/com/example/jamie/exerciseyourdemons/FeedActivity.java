package com.example.jamie.exerciseyourdemons;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class FeedActivity extends AppCompatActivity {
  DBHelper dbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed);
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);


    dbHelper = new DBHelper(this);

    ArrayList<Workout> workoutList = dbHelper.allWorkouts();
    Log.d("Workouts List:", workoutList.toString());

    WorkoutsAdapter workoutsAdapter = new WorkoutsAdapter(this, workoutList);
    ListView listView = findViewById(R.id.workout_list_view);
    listView.setAdapter(workoutsAdapter);
  }

  public void openDetails(View listItem) {
    Workout workout = (Workout) listItem.getTag();
    Intent i = new Intent(this, WorkoutDetailsActivity.class);
    i.putExtra("id", workout.getId());
    startActivity(i);
  }

  public void addWorkout(View view) {
    Intent i = new Intent(this, WorkoutEditActivity.class);
    i.putExtra("id", 0);
    startActivity(i);
  }
}

