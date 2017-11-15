package com.example.jamie.exerciseyourdemons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jamie on 11/11/2017.
 */

public class Workout {
  private int id;
  private ExerciseType type;
  private Date date;
  private int distance; // In metres.
  private double speed; // In metres per second.
  private int time; // In seconds.
  private Location location;
  private String description;

  public Workout(ExerciseType type, Date date, int distance, int time, Location location, String description) {
    this.type = type;
    this.date = date;
    this.distance = distance;
    this.speed = Double.valueOf(distance)/time;
    this.location = location;
    this.description = description;
    this.time = time;
  }

  public Workout(int id, ExerciseType type, Date date, int distance, int time, Location location, String description) {
    this.id = id;
    this.type = type;
    this.date = date;
    this.distance = distance;
    this.speed = Double.valueOf(distance)/time;
    this.location = location;
    this.description = description;
    this.time = time;
  }

  public Location getLocation() {
    return location;
  }

  public int getId() {
    return id;
  }

  public ExerciseType getType() {
    return type;
  }

  public int getTypeId() {
    return type.getId();
  }

  public int getDistance() {
    return distance;
  }

  public double getSpeed() {
    return speed;
  }

  public String getDescription() {
    return description;
  }

  public String getLatitudeString() {
    return String.valueOf(location.getLatitude());
  }

  public String getLongitudeString() {
    return String.valueOf(location.getLongitude());
  }

  public Date getDate() {
    return date;
  }

  public long getDateLong() {
    return date.getTime();
  }

  public int getTime() {
    return time;
  }

  public int getTimeInMinutes() {
    return time/60;
  }

  public String getPrettyTime() {
    return String.format(Locale.UK, "%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));
  }

  public String getPrettyDate() {
    SimpleDateFormat dF = new SimpleDateFormat("d/M/yy H:m", Locale.UK);
    return dF.format(date);
  }

  public String getPrettyDistance() {
    if (distance < 1000) {
      return String.format(Locale.UK, "%d metres", distance);
    } else {
      return String.format(Locale.UK, "%.1f km", (Double.valueOf(distance) / 1000));
    }
  }

  public String getPrettySpeed() {
    double distance = this.distance / 1000.0;
    double time = this.time / 3600.0;
    String speed = String.format(Locale.UK, "%.2f", distance / time);
    return speed + DistanceUnit.KILOMETRES.getSpeedAbbr();
  }

  public String getFeedSummary() {
    SimpleDateFormat dF = new SimpleDateFormat("EEEE", Locale.UK);
    String weekday = dF.format(date);
    return weekday + " " + type.getName() + ", " + distance/1000 + "km, " + getTimeInMinutes() + "minutes";
  }

  public String getDetailsTitle() {
    SimpleDateFormat dF = new SimpleDateFormat("EEEE", Locale.UK);
    String weekday = dF.format(date);
    return weekday + " " + type.getName();
  }

  public int save(Context context) {
    DBHelper dbHelper = new DBHelper(context);
    id = dbHelper.save(this);
    saveMiniMap(context);
    return id;
  }

  public void update(Context context) {
    DBHelper dbHelper = new DBHelper(context);
    dbHelper.update(this);
  }

  private void saveMiniMap(final Context context) {
    String url = "https://maps.googleapis.com/maps/api/staticmap?markers=";
    url += location.getLatitude() + ",";
    url += location.getLongitude();
    url += "&size=500x160&scale=2";
    url += "&key=" + context.getString(R.string.google_maps_key);

    final String filepath = id + "_mini_map";

    class downloadImage extends AsyncTask<String, Void, Bitmap> {
      @Override
      protected Bitmap doInBackground(String... url) {
        try {
          URL imageUrl = new URL(url[0]);
          return BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
        } catch (IOException e) {
          e.printStackTrace();
        }
        return null;
      }

      protected void onPostExecute(Bitmap result) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        try {
          FileOutputStream fos = context.openFileOutput(filepath, Context.MODE_PRIVATE);
          fos.write(bytes.toByteArray());
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    new downloadImage().execute(url);
  }

  public Bitmap fetchMiniMap(Context context) {
    String filename = id + "_mini_map";
    try {
      FileInputStream fis = context.openFileInput(filename);
      Bitmap image = BitmapFactory.decodeStream(fis);
      fis.close();
      return image;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void deleteMiniMap(Context context) {
    context.deleteFile(id + "_mini_map");
  }
}
