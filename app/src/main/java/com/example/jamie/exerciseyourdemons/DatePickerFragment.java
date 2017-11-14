package com.example.jamie.exerciseyourdemons;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * Created by jamie on 14/11/2017.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
  public interface DatePickerListener {
    void receiveDate(int year, int month, int day);
  }
  DatePickerListener listener;

  public static DatePickerFragment newInstance(int year, int month, int day) {
    DatePickerFragment dpf = new DatePickerFragment();

    Bundle args = new Bundle();
    args.putInt("year", year);
    args.putInt("month", month);
    args.putInt("day", day);

    dpf.setArguments(args);
    return dpf;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    int year = getArguments().getInt("year");
    int month = getArguments().getInt("month");
    int day = getArguments().getInt("day");
    listener = (DatePickerListener) getActivity();

    return new DatePickerDialog(getActivity(), this, year, month, day);
  }

  public void onDateSet(DatePicker view, int year, int month, int day) {
    listener.receiveDate(year, month, day);
  }
}
