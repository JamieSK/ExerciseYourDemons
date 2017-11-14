package com.example.jamie.exerciseyourdemons;

import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

import java.util.Calendar;
import java.util.Locale;

public class WorkoutEditActivity extends AppCompatActivity implements
        DurationDialogFragment.DurationDialogListener,
        DistanceDialogFragment.DistanceDialogListener,
        TimePickerFragment.TimePickerListener,
        DatePickerFragment.DatePickerListener {

  Button durationButton;
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


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_workout_edit);
    dbHelper = new DBHelper(this);

    durationButton = findViewById(R.id.editDurationButton);
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

    dateButton = findViewById(R.id.editDateButton);
    Calendar cal = Calendar.getInstance();
    year = cal.get(Calendar.YEAR);
    month = cal.get(Calendar.MONTH);
    day = cal.get(Calendar.DAY_OF_MONTH);

    timeButton = findViewById(R.id.editTimeButton);
    hourTime = cal.get(Calendar.HOUR_OF_DAY);
    minuteTime = cal.get(Calendar.MINUTE);

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
      durationButton.setText(String.format(Locale.UK, "%d:%02d:%02d", hours, minutes, seconds));
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

  public void datePicker(View view) {
    DialogFragment date = DatePickerFragment.newInstance(year, month, day);
    date.show(getFragmentManager(), "date picker");
  }

  @Override
  public void receiveDate(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
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
    timeButton.setText(String.format(Locale.UK, "%d:%02d", hourTime, minuteTime));
  }
}
