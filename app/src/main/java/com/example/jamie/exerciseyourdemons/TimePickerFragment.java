package com.example.jamie.exerciseyourdemons;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by jamie on 14/11/2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
  public interface TimePickerListener {
    void receiveTime(int hourOfDay, int minute);
  }
  TimePickerListener listener;

  public static TimePickerFragment newInstance(int hourOfDay, int minute) {
    TimePickerFragment tpf = new TimePickerFragment();

    Bundle args = new Bundle();
    args.putInt("hourOfDay", hourOfDay);
    args.putInt("minute", minute);

    tpf.setArguments(args);
    return tpf;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    int hour = getArguments().getInt("hourOfDay");
    int minute = getArguments().getInt("minute");
    listener = (TimePickerListener) getActivity();

    return new TimePickerDialog(getActivity(), this, hour, minute, true);
  }

  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    listener.receiveTime(hourOfDay, minute);
  }
}
