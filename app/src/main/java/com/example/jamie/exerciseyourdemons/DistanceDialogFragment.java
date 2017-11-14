package com.example.jamie.exerciseyourdemons;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by jamie on 14/11/2017.
 */

public class DistanceDialogFragment extends DialogFragment {
  public interface DistanceDialogListener {
    void receiveDistance(int distanceInts, int distanceDecimal, DistanceUnit distanceUnit);
  }

  DistanceDialogListener listener;
  NumberPicker intsPicker;
  NumberPicker decimalPicker;
  NumberPicker unitPicker;

  public static DistanceDialogFragment newInstance(int distanceInts, int distanceDecimal, DistanceUnit distanceUnit) {
    DistanceDialogFragment ddf = new DistanceDialogFragment();

    Bundle args = new Bundle();
    args.putInt("ints", distanceInts);
    args.putInt("decimal", distanceDecimal);
    args.putInt("unit", distanceUnit.getNum());

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

    int ints = getArguments().getInt("ints", 0);
    int decimal = getArguments().getInt("decimal", 0);
    int unit = getArguments().getInt("unit", 0);

    intsPicker = new NumberPicker(getActivity());
    intsPicker.setMaxValue(999);
    intsPicker.setMinValue(0);
    intsPicker.setValue(ints);
    intsPicker.setWrapSelectorWheel(false);
    linLayoutH.addView(intsPicker);

    decimalPicker = new NumberPicker(getActivity());
    decimalPicker.setMaxValue(9);
    decimalPicker.setMinValue(0);
    String[] values = new String[] {".0", ".1", ".2", ".3", ".4", ".5", ".6", ".7", ".8", ".9"};
    decimalPicker.setDisplayedValues(values);
    decimalPicker.setValue(decimal);
    linLayoutH.addView(decimalPicker);

    unitPicker = new NumberPicker(getActivity());
    unitPicker.setMaxValue(2);
    unitPicker.setMinValue(1);
    unitPicker.setDisplayedValues(new String[] {"mi", "km"});
    unitPicker.setValue(unit);
    linLayoutH.addView(unitPicker);

    LinearLayout linLayoutV = new LinearLayout(getActivity());
    linLayoutV.setOrientation(LinearLayout.VERTICAL);
    linLayoutV.addView(linLayoutH);

    Button okButton = new Button(getActivity());
    okButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        int ints = intsPicker.getValue();
        int decimal = decimalPicker.getValue();
        int unit = unitPicker.getValue();
        listener.receiveDistance(ints, decimal, DistanceUnit.unitFromNum(unit));
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
    listener = (DistanceDialogListener) context;
  }

  @Override
  public void onSaveInstanceState(Bundle outState){
    super.onSaveInstanceState(outState);
    outState.putInt("ints", intsPicker.getValue());
    outState.putInt("decimal", decimalPicker.getValue());
    outState.putInt("unit", unitPicker.getValue());
  }
}
