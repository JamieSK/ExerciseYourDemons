package com.example.jamie.exerciseyourdemons;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

public class WorkoutDetailsActivity extends AppCompatActivity implements DeleteConfirmationDialogFragment.DeleteDialogListener {
  private Workout workout;
  private TextView detailsDateTimeTextView;
  private TextView detailsTimeTextView;
  private TextView detailsDistanceTextView;
  private MapView detailsMapView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_workout_details);
    DBHelper dbHelper = new DBHelper(this);

    int id = getIntent().getIntExtra("id", 0);
    workout = dbHelper.findWorkout(id);

    setTitle(workout.getDetailsTitle());
    detailsDateTimeTextView = findViewById(R.id.detailsDateTimeTextView);
    detailsDateTimeTextView.setText(workout.getPrettyDate());

    detailsTimeTextView = findViewById(R.id.detailsTimeTextView);
    detailsTimeTextView.setText(workout.getPrettyTime());

    detailsDistanceTextView = findViewById(R.id.detailsDistanceTextView);
    detailsDistanceTextView.setText(workout.getPrettyDistance());

    detailsMapView = findViewById(R.id.detailsMapView);
//    detailsMapView.getMapAsync();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.edit_delete_menu, menu);
    return true;
  }

  public boolean onOptionsItemSelected(MenuItem item){
    if (item.getItemId() == R.id.menu_edit) {
      Intent intent = new Intent(this, WorkoutEditActivity.class);
      startActivity(intent);
      return true;
    } else if (item.getItemId() == R.id.menu_delete) {
      DialogFragment deleteConfirmation = new DeleteConfirmationDialogFragment();
      deleteConfirmation.show(getFragmentManager(), "delete confirmation");

      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void onConfirmDelete() {
    DBHelper dbHelper = new DBHelper(this);
    dbHelper.delete(workout, this);

    Intent intent = new Intent(this, FeedActivity.class);
    startActivity(intent);
  }
}
