package com.example.jamie.exerciseyourdemons;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class WorkoutDetailsActivity extends AppCompatActivity
        implements DeleteConfirmationDialogFragment.DeleteDialogListener, OnMapReadyCallback {
  private Workout workout;
  private TextView detailsDateTimeTextView;
  private TextView detailsTimeTextView;
  private TextView detailsDistanceTextView;
  private TextView detailsSpeedView;
  private MapFragment detailsMapFragment;

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

    detailsSpeedView = findViewById(R.id.detailsSpeedTextView);
    detailsSpeedView.setText(workout.getPrettySpeed());

    detailsMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.editMapFragment);
    detailsMapFragment.getMapAsync(this);
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
      intent.putExtra("id", workout.getId());
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

  @Override
  public void onMapReady(GoogleMap map) {
    double latitude = workout.getLocation().getLatitude();
    double longitude = workout.getLocation().getLongitude();
    LatLng position = new LatLng(latitude, longitude);

    map.addMarker(new MarkerOptions().position(position).title("Start"));
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
  }
}
