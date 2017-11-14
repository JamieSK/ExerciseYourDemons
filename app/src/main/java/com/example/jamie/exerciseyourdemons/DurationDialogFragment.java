package com.example.jamie.exerciseyourdemons;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class DurationDialogFragment extends DialogFragment {
  public interface DurationDialogListener {
    void receiveDuration(int hours, int minutes, int seconds);
  }

  DurationDialogListener listener;
  NumberPicker hoursPicker;
  NumberPicker minutesPicker;
  NumberPicker secondsPicker;

  public static DurationDialogFragment newInstance(int hours, int minutes, int seconds) {
    DurationDialogFragment ddf = new DurationDialogFragment();

    Bundle args = new Bundle();
    args.putInt("hours", hours);
    args.putInt("minutes", minutes);
    args.putInt("seconds", seconds);

    ddf.setArguments(args);
    return ddf;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    LinearLayout linLayoutH = new LinearLayout(getActivity());
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    linLayoutH.setLayoutParams(params);
    linLayoutH.setGravity(Gravity.CENTER);

    int hours = getArguments().getInt("hours", 0);
    int minutes = getArguments().getInt("minutes", 0);
    int seconds = getArguments().getInt("seconds", 0);

    hoursPicker = new NumberPicker(getActivity());
    hoursPicker.setMaxValue(23);
    hoursPicker.setMinValue(0);
    hoursPicker.setValue(hours);
    linLayoutH.addView(hoursPicker);

    TextView h = new TextView(getActivity());
    h.setText("h");
    linLayoutH.addView(h);

    minutesPicker = new NumberPicker(getActivity());
    minutesPicker.setMaxValue(59);
    minutesPicker.setMinValue(0);
    minutesPicker.setValue(minutes);
    linLayoutH.addView(minutesPicker);

    TextView m = new TextView(getActivity());
    m.setText("m");
    linLayoutH.addView(m);

    secondsPicker = new NumberPicker(getActivity());
    secondsPicker.setMaxValue(59);
    secondsPicker.setMinValue(0);
    secondsPicker.setValue(seconds);
    linLayoutH.addView(secondsPicker);

    TextView s = new TextView(getActivity());
    s.setText("s");
    linLayoutH.addView(s);

    LinearLayout linLayoutV = new LinearLayout(getActivity());
    linLayoutV.setOrientation(LinearLayout.VERTICAL);
    linLayoutV.addView(linLayoutH);

    Button okButton = new Button(getActivity());
    okButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        int hours = hoursPicker.getValue();
        int minutes = minutesPicker.getValue();
        int seconds = secondsPicker.getValue();
        listener.receiveDuration(hours, minutes, seconds);
        dismiss();
      }
    });

    params.gravity = Gravity.CENTER_HORIZONTAL;
    okButton.setLayoutParams(params);
    okButton.setText(R.string.ok);
    linLayoutV.addView(okButton);
    linLayoutV.setPadding(16, 16, 16, 16);


    return linLayoutV;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    listener = (DurationDialogListener) context;
  }

  @Override
  public void onSaveInstanceState(Bundle outState){
    super.onSaveInstanceState(outState);
    outState.putInt("hours", hoursPicker.getValue());
    outState.putInt("minutes", minutesPicker.getValue());
    outState.putInt("seconds", secondsPicker.getValue());
  }
}
