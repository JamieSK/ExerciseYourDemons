package com.example.jamie.exerciseyourdemons;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jamie on 11/11/2017.
 */

public class WorkoutsAdapter extends ArrayAdapter<Workout> {
  public WorkoutsAdapter(Context context, ArrayList<Workout> workouts) {
    super(context, 0, workouts);
  }

  @NonNull
  public View getView(int position, View view, @NonNull ViewGroup parent) {
    if (view == null) {
      view = LayoutInflater.from(getContext()).inflate(R.layout.workout_item, parent, false);
    }

    Workout currentWorkout = getItem(position);

    TextView title = view.findViewById(R.id.workoutListTextView);
    title.setText(currentWorkout.getFeedSummary());

    ImageView miniMap = view.findViewById(R.id.workoutListMap);
    miniMap.setImageBitmap(currentWorkout.fetchMiniMap(getContext()));

    view.setTag(currentWorkout);
    return view;
  }
}
