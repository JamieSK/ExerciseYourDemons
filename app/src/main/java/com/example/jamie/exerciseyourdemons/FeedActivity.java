package com.example.jamie.exerciseyourdemons;

import android.content.Intent;
import android.location.Location;
import android.location.LocationProvider;
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
}



//  ExerciseType type = new ExerciseType("Run");
//    type.save(this);
//
//            Location location = new Location("");
//            location.setLatitude(56.8793776);
//            location.setLongitude(-4.2818013);
//            Workout workout = new Workout(type, new Date(), 10000, 5, location, "Stuff");
//            workout.save(this);

