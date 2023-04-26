package com.example.ontrack;

public class ActivityLog {

    private String date;
    private String activityName;
    private String userNotes;
    private int minutes;

    ActivityLog(String date, String activityName, String userNotes, int minutes) {
        super();
        this.date = date;
        this.activityName = activityName;
        this.userNotes = userNotes;
        this.minutes = minutes;
    }

    public String getDate() { return date; }
    public String getActivityName() { return activityName; }
    public String getUserNotes() { return userNotes; }
    public String getMinutes() { return "" + minutes; }



}
