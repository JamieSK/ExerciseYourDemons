package com.example.jamie.exerciseyourdemons;

import android.content.Context;

/**
 * Created by jamie on 11/11/2017.
 */

public class ExerciseType {
  private int id;
  private String name;

  public ExerciseType(String name) {
    this.name = name;
  }

  public ExerciseType(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void save(Context context) {
    DBHelper dbHelper = new DBHelper(context);
    id = dbHelper.save(this);
  }
}
