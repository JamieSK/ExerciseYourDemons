package com.example.jamie.exerciseyourdemons;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.MapFragment;

import java.util.Locale;

public class WorkoutEditActivity extends AppCompatActivity implements DurationDialogFragment.DurationDialogListener, DistanceDialogFragment.DistanceDialogListener {

  Button timeButton;
  int hours;
  int minutes;
  int seconds;

  Button distanceButton;
  int distanceInts;
  int distanceDecimal;
  DistanceUnit distanceUnit;

  Button speedButton;
  MapFragment mapFragment;
  Spinner typeSpinner;
  EditText dateView;
  EditText timeView;
  EditText descriptionView;
  Button saveButton;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_workout_edit);

    timeButton = findViewById(R.id.editTimeButton);
    hours = 0;
    minutes = 0;
    seconds = 0;

    distanceButton = findViewById(R.id.distanceButton);
    distanceInts = 0;
    distanceDecimal = 0;
    distanceUnit = DistanceUnit.KILOMETRES;

    speedButton = findViewById(R.id.speedButton);

    mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.editMapFragment);
    typeSpinner = findViewById(R.id.exerciseTypeSpinner);
    dateView = findViewById(R.id.editDateView);
    timeView = findViewById(R.id.editTimeView);
    descriptionView = findViewById(R.id.editDescriptionView);
    saveButton = findViewById(R.id.editSaveButton);
  }

  public void durationPicker(View view) {
    DialogFragment duration = DurationDialogFragment.newInstance(hours, minutes, seconds);
    duration.show(getFragmentManager(), "duration picker");
  }

  public void receiveDuration(int hours, int minutes, int seconds) {
    this.hours = hours;
    this.minutes = minutes;
    this.seconds = seconds;
    if (durationSet()) {
      timeButton.setText(String.format(Locale.UK, "%d:%02d:%02d", hours, minutes, seconds));
    }
    setSpeed();
  }

  private boolean durationSet() {
    return !(hours == 0 && minutes == 0 && seconds == 0);
  }

  public void distancePicker(View view) {
    DialogFragment distance = DistanceDialogFragment.newInstance(distanceInts, distanceDecimal, distanceUnit);
    distance.show(getFragmentManager(), "distance picker");
  }

  public void receiveDistance(int distanceInts, int distanceDecimal, DistanceUnit distanceUnit) {
    this.distanceInts = distanceInts;
    this.distanceDecimal = distanceDecimal;
    this.distanceUnit = distanceUnit;

    if(distanceSet()) {
      distanceButton.setText(String.format(Locale.UK, "%d.%d%s", distanceInts, distanceDecimal, distanceUnit.getAbbr()));
    }
    setSpeed();
  }

  private boolean distanceSet() {
    return !(distanceInts == 0 && distanceDecimal == 0 && distanceUnit == DistanceUnit.KILOMETRES);
  }

  private void setSpeed() {
    if (durationSet() && distanceSet()) {
      double distance = distanceInts + distanceDecimal * 0.1;
      double time = hours + minutes/60.0 + seconds/3600.0;
      String speed = String.format(Locale.UK, "%.2f", distance / time);
      speedButton.setText(speed + distanceUnit.getSpeedAbbr());
    }
  }


}
