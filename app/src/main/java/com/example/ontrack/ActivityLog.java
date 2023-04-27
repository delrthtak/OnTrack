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

    ActivityLog() {
        super();
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMinutes(String minutes) {
        this.minutes = Integer.valueOf(minutes);
    }

    public void setUserNotes(String userNotes) {
        this.userNotes = userNotes;
    }


    public String getDate() { return date; }
    public String getActivityName() { return activityName; }
    public String getUserNotes() { return userNotes; }
    public String getMinutes() { return "" + minutes; }



}
