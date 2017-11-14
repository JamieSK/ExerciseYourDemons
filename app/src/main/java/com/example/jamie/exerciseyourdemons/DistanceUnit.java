package com.example.jamie.exerciseyourdemons;

/**
 * Created by jamie on 14/11/2017.
 */

public enum DistanceUnit {
  MILES("miles", "mph", 1),
  KILOMETRES("km", "km/h",  2);

  private String abbr;
  private String speedAbbr;
  private int num;

  DistanceUnit(String abbr, String speedAbbr, int num) {
    this.abbr = abbr;
    this.speedAbbr = speedAbbr;
    this.num = num;
  }

  public String getAbbr() {
    return abbr;
  }

  public String getSpeedAbbr() {
    return speedAbbr;
  }

  public int getNum() {
    return num;
  }

  public static DistanceUnit unitFromNum(int num) {
    for (DistanceUnit unit : DistanceUnit.values()) {
      if (unit.getNum() == num) {
        return unit;
      }
    }
    return null;
  }
}
