package com.example.jamie.exerciseyourdemons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SpinnerAdapter;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jamie on 11/11/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "exercise.db";
  public static final String WORKOUT_TYPE_TABLE_NAME = "workout_type";
  public static final String WORKOUT_TYPE_COLUMN_ID = "type_id";
  public static final String WORKOUT_TYPE_COLUMN_NAME = "name";
  public static final String WORKOUT_TABLE_NAME = "workout";
  public static final String WORKOUT_COLUMN_ID = "workout_id";
  public static final String WORKOUT_COLUMN_TYPE_ID = "type_id";
  public static final String WORKOUT_COLUMN_ROUTE_ID = "route_id";
  public static final String WORKOUT_COLUMN_DATETIME = "datetime";
  public static final String WORKOUT_COLUMN_DISTANCE = "distance";
  public static final String WORKOUT_COLUMN_SPEED = "speed";
  public static final String WORKOUT_COLUMN_LATITUDE = "latitude";
  public static final String WORKOUT_COLUMN_LONGITUDE = "longitude";
  public static final String WORKOUT_COLUMN_DESCRIPTION = "description";

  public DBHelper(Context context) {
    super(context, DATABASE_NAME, null, 1);
  }

  public void onConfigure(SQLiteDatabase db) {
    db.setForeignKeyConstraintsEnabled(true);
  }

  public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + WORKOUT_TYPE_TABLE_NAME + "("
            + WORKOUT_TYPE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + WORKOUT_TYPE_COLUMN_NAME + " TEXT);");
    db.execSQL("CREATE TABLE " + WORKOUT_TABLE_NAME + "("
            + WORKOUT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + WORKOUT_COLUMN_TYPE_ID + " INTEGER REFERENCES " + WORKOUT_TYPE_TABLE_NAME + "(" + WORKOUT_TYPE_COLUMN_ID + ") ON DELETE CASCADE, "
            + WORKOUT_COLUMN_DATETIME + " INTEGER, "
            + WORKOUT_COLUMN_DISTANCE + " INTEGER, "
            + WORKOUT_COLUMN_SPEED + " INTEGER, "
            + WORKOUT_COLUMN_LATITUDE + " TEXT, "
            + WORKOUT_COLUMN_LONGITUDE + " TEXT, "
            + WORKOUT_COLUMN_DESCRIPTION + " TEXT);" );
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.setForeignKeyConstraintsEnabled (true);
    db.execSQL("DROP TABLE IF EXISTS " + WORKOUT_TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + WORKOUT_TYPE_TABLE_NAME);
    onCreate(db);
  }

  public int save(ExerciseType type) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(WORKOUT_TYPE_COLUMN_NAME, type.getName());

    return (int) db.insert(WORKOUT_TYPE_TABLE_NAME, null, cv);
  }

  public int save(Workout workout) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();

    cv.put(WORKOUT_COLUMN_TYPE_ID, workout.getTypeId());
    cv.put(WORKOUT_COLUMN_DATETIME, workout.getDateLong());
    cv.put(WORKOUT_COLUMN_DISTANCE, workout.getDistance());
    cv.put(WORKOUT_COLUMN_SPEED, workout.getSpeed());
    cv.put(WORKOUT_COLUMN_LATITUDE, workout.getLatitudeString());
    cv.put(WORKOUT_COLUMN_LONGITUDE, workout.getLongitudeString());
    cv.put(WORKOUT_COLUMN_DESCRIPTION, workout.getDescription());

    return (int) db.insert(WORKOUT_TABLE_NAME, null, cv);
  }

  public void delete(Workout workout, Context context) {
    workout.deleteMiniMap(context);

    SQLiteDatabase db = getWritableDatabase();
    db.delete(WORKOUT_TABLE_NAME, WORKOUT_COLUMN_ID + " = ?", new String[] {String.valueOf(workout.getId())});
  }

  public ExerciseType findExerciseType(int id) {
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.query(WORKOUT_TYPE_TABLE_NAME, null,
            WORKOUT_TYPE_COLUMN_ID + " = ?", new String[] {String.valueOf(id)},
            null, null, null);
    cursor.moveToFirst();

    int type_id = cursor.getInt(cursor.getColumnIndex(WORKOUT_TYPE_COLUMN_ID));
    String name = cursor.getString(cursor.getColumnIndex(WORKOUT_TYPE_COLUMN_NAME));

    cursor.close();
    return new ExerciseType(type_id, name);
  }

  public Workout findWorkout(int id) {
    SQLiteDatabase db = getReadableDatabase();
    Cursor cursor = db.query(WORKOUT_TABLE_NAME, null,
            WORKOUT_COLUMN_ID + " = ?", new String[] {String.valueOf(id)},
            null, null, null);
    cursor.moveToFirst();

    int workoutId = cursor.getInt(cursor.getColumnIndex(WORKOUT_COLUMN_ID));
    int typeId = cursor.getInt(cursor.getColumnIndex(WORKOUT_COLUMN_TYPE_ID));
    ExerciseType exerciseType = findExerciseType(typeId);
    Date date = new Date(cursor.getLong(cursor.getColumnIndex(WORKOUT_COLUMN_DATETIME)));
    int distance = cursor.getInt(cursor.getColumnIndex(WORKOUT_COLUMN_DISTANCE));
    int speed = cursor.getInt(cursor.getColumnIndex(WORKOUT_COLUMN_SPEED));
    Location location = new Location("");
    location.setLatitude(Double.valueOf(cursor.getString(cursor.getColumnIndex(WORKOUT_COLUMN_LATITUDE))));
    location.setLongitude(Double.valueOf(cursor.getString(cursor.getColumnIndex(WORKOUT_COLUMN_LONGITUDE))));
    String description = cursor.getString(cursor.getColumnIndex(WORKOUT_COLUMN_DESCRIPTION));

    return new Workout(workoutId, exerciseType, date, distance, speed, location, description);
  }

  public ArrayList<ExerciseType> allExerciseTypes() {
    ArrayList<ExerciseType> types = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.rawQuery("SELECT * FROM " + WORKOUT_TYPE_TABLE_NAME, null);
    cursor.moveToFirst();

    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndex(WORKOUT_TYPE_COLUMN_ID));
      String name = cursor.getString(cursor.getColumnIndex(WORKOUT_TYPE_COLUMN_NAME));

      ExerciseType type = new ExerciseType(id, name);
      types.add(type);
    }
    cursor.close();
    return types;
  }

  public ArrayList<Workout> allWorkouts() {
    ArrayList<Workout> workouts = new ArrayList<>();
    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.rawQuery("SELECT * FROM " + WORKOUT_TABLE_NAME, null);
    cursor.moveToFirst();

    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndex(WORKOUT_COLUMN_ID));
      int typeId = cursor.getInt(cursor.getColumnIndex(WORKOUT_COLUMN_TYPE_ID));
      ExerciseType type = findExerciseType(typeId);
      Date date = new Date(cursor.getLong(cursor.getColumnIndex(WORKOUT_COLUMN_DATETIME)));
      int distance = cursor.getInt(cursor.getColumnIndex(WORKOUT_COLUMN_DISTANCE));
      int speed = cursor.getInt(cursor.getColumnIndex(WORKOUT_COLUMN_SPEED));
      Location location = new Location("");
      location.setLatitude(Double.valueOf(cursor.getString(cursor.getColumnIndex(WORKOUT_COLUMN_LATITUDE))));
      location.setLongitude(Double.valueOf(cursor.getString(cursor.getColumnIndex(WORKOUT_COLUMN_LONGITUDE))));
      String description = cursor.getString(cursor.getColumnIndex(WORKOUT_COLUMN_DESCRIPTION));

      Workout workout = new Workout(id, type, date, distance, speed, location, description);
      workouts.add(workout);
    }
    cursor.close();
    return workouts;
  }

  public Cursor getTypeCursor(Context context) {
    SQLiteDatabase db = getReadableDatabase();

    Cursor cursor = db.rawQuery("SELECT " + WORKOUT_TYPE_COLUMN_ID + " AS _id, " + WORKOUT_TYPE_COLUMN_NAME + " FROM " + WORKOUT_TYPE_TABLE_NAME, null);

    return cursor;
  }
}
