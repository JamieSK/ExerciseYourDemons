package com.example.jamie.exerciseyourdemons;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WorkoutEditActivity extends AppCompatActivity implements DurationDialogFragment.DurationDialogListener, DistanceDialogFragment.DistanceDialogListener, TimePickerFragment.TimePickerListener, DatePickerFragment.DatePickerListener, OnMapReadyCallback, AdapterView.OnItemSelectedListener {

  int id;

  Button durationButton;
  int hours;
  int minutes;
  int seconds;

  Button distanceButton;
  int distanceInts;
  int distanceDecimal;
  DistanceUnit distanceUnit;

  Button speedButton;

  FusedLocationProviderClient mFusedLocationClient;
  MapFragment mapFragment;
  Location currentLocation;

  Spinner typeSpinner;
  int typeId;

  Button dateButton;
  int year;
  int month;
  int day;

  Button timeButton;
  int hourTime;
  int minuteTime;

  EditText descriptionView;
  Button saveButton;

  DBHelper dbHelper;
  Calendar cal;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_workout_edit);
    dbHelper = new DBHelper(this);

    durationButton = findViewById(R.id.editDurationButton);
    distanceButton = findViewById(R.id.distanceButton);
    speedButton = findViewById(R.id.speedButton);
    dateButton = findViewById(R.id.editDateButton);
    cal = Calendar.getInstance();
    timeButton = findViewById(R.id.editTimeButton);
    descriptionView = findViewById(R.id.editDescriptionView);
    saveButton = findViewById(R.id.editSaveButton);

    getSpinner();

    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.editMapFragment);

    id = getIntent().getExtras().getInt("id", 0);
    if (id == 0) {
      setTitle("Add Workout");
      newWorkout();
    } else {
      updateWorkout(dbHelper.findWorkout(id));
    }
  }

  private void newWorkout() {
    hours = 0;
    minutes = 0;
    seconds = 0;

    distanceInts = 0;
    distanceDecimal = 0;
    distanceUnit = DistanceUnit.KILOMETRES;

    year = cal.get(Calendar.YEAR);
    month = cal.get(Calendar.MONTH);
    day = cal.get(Calendar.DAY_OF_MONTH);
    hourTime = cal.get(Calendar.HOUR_OF_DAY);
    minuteTime = cal.get(Calendar.MINUTE);

    mapFragment.getMapAsync(this);
  }

  private void updateWorkout(Workout workout) {
    int time = workout.getTime();
    hours = time / 3600;
    minutes = time / 60;
    seconds = time % 60;
    setDurationText();

    int distance = workout.getDistance();
    distanceInts = distance / 1000;
    distanceDecimal = (distance % 1000) / 100;
    distanceUnit = DistanceUnit.KILOMETRES;
    setDistanceText();
    setSpeed();

    Date date = workout.getDate();
    cal.setTime(date);
    year = cal.get(Calendar.YEAR) - 1900;
    month = cal.get(Calendar.MONTH) + 1;
    day = cal.get(Calendar.DAY_OF_MONTH);
    minuteTime = date.getMinutes();
    setDateText();
    setTimeText();

    descriptionView.setText(workout.getDescription());
    typeSpinner.setSelection(workout.getTypeId() - 1);

    currentLocation = workout.getLocation();
    mapFragment.getMapAsync(this);
  }

  private void getSpinner() {
    typeSpinner = findViewById(R.id.exerciseTypeSpinner);
    Cursor typeCursor = dbHelper.getTypeCursor(this);
    CursorAdapter adapter = new CursorAdapter(this, typeCursor, 0) {
      @Override
      public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
      }

      @Override
      public void bindView(View view, Context context, Cursor cursor) {
        TextView text = (TextView) view;
        text.setText(cursor.getString(cursor.getColumnIndex("name")));
      }
    };
    typeSpinner.setAdapter(adapter);
  }

  public void durationPicker(View view) {
    DialogFragment duration = DurationDialogFragment.newInstance(hours, minutes, seconds);
    duration.show(getFragmentManager(), "duration picker");
  }

  public void receiveDuration(int hours, int minutes, int seconds) {
    this.hours = hours;
    this.minutes = minutes;
    this.seconds = seconds;
    setDurationText();
    setSpeed();
  }

  private void setDurationText() {
    if (durationSet()) {
      durationButton.setText(String.format(Locale.UK, "%d:%02d:%02d", hours, minutes, seconds));
    }
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

    setDistanceText();
    setSpeed();
  }

  private void setDistanceText() {
    if (distanceSet()) {
      distanceButton.setText(String.format(Locale.UK, "%d.%d%s", distanceInts, distanceDecimal, distanceUnit.getAbbr()));
    }
  }

  private boolean distanceSet() {
    return !(distanceInts == 0 && distanceDecimal == 0 && distanceUnit == DistanceUnit.KILOMETRES);
  }

  private void setSpeed() {
    if (durationSet() && distanceSet()) {
      double distance = distanceInts + distanceDecimal * 0.1;
      double time = hours + minutes / 60.0 + seconds / 3600.0;
      String speed = String.format(Locale.UK, "%.2f", distance / time);
      speedButton.setText(speed + distanceUnit.getSpeedAbbr());
    }
  }

  public void datePicker(View view) {
    DialogFragment date = DatePickerFragment.newInstance(year, month, day);
    date.show(getFragmentManager(), "date picker");
  }

  @Override
  public void receiveDate(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
    setDateText();
  }

  private void setDateText() {
    dateButton.setText(String.format(Locale.UK, "%d/%d/%d", day, month, year));
  }

  public void timePicker(View view) {
    DialogFragment time = TimePickerFragment.newInstance(hourTime, minuteTime);
    time.show(getFragmentManager(), "time picker");
  }

  @Override
  public void receiveTime(int hourOfDay, int minute) {
    hourTime = hourOfDay;
    minuteTime = minute;
    setTimeText();
  }

  private void setTimeText() {
    timeButton.setText(String.format(Locale.UK, "%d:%02d", hourTime, minuteTime));
  }

  @Override
  public void onMapReady(final GoogleMap map) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }

    if (id == 0) {
      mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
          if (location != null) {
            currentLocation = location;
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng position = new LatLng(latitude, longitude);

            map.addMarker(new MarkerOptions().position(position).title("Start"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
          }
        }
      });
    } else {
      double latitude = currentLocation.getLatitude();
      double longitude = currentLocation.getLongitude();
      LatLng position = new LatLng(latitude, longitude);

      map.addMarker(new MarkerOptions().position(position).title("Start"));
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
  }

  public void onSave(View view) {
    SQLiteCursor cursor = (SQLiteCursor) typeSpinner.getSelectedItem();
    typeId = Integer.valueOf(cursor.getString(0));
    ExerciseType type = dbHelper.findExerciseType(typeId);
    cal.set(year + 1900, month, day, hourTime, minuteTime);
    Date date = cal.getTime();
    int distance;
    if (distanceUnit == DistanceUnit.KILOMETRES) {
      distance = (distanceInts * 1000) + (distanceDecimal * 100);
    } else {
      distance = (distanceInts * 1600) + (distanceDecimal * 160);
    }
    int time = (hours*3600 + minutes*60 + seconds);
    String description = descriptionView.getText().toString();

    if (id == 0) {
      Workout workout = new Workout(type, date, distance, time, currentLocation, description);
      id = workout.save(this);
    } else {
      Workout workout = new Workout(id, type, date, distance, time, currentLocation, description);
      workout.update(this);
    }

    Intent i = new Intent(this, WorkoutDetailsActivity.class);
    i.putExtra("id", id);
    startActivity(i);
  }
}
