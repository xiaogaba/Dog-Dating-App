package com.example.xin.firex;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Playdate {
    public ArrayList<String> attendees;     // user emails
    public Date date;   // (yr, month, date, hrs, mins)
    public Location meetingPlace;   // Location.getLatitude, getLongitude

    public Playdate(ArrayList<String> attendees, Date date, Location meetingPlace) {
        this.attendees = attendees;
        this.date = date;
        this.meetingPlace = meetingPlace;
    }

    public String dateToDBString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public String dToString() {
        SimpleDateFormat format = new SimpleDateFormat("E MMM d, y  HH:mm");
        return format.format(date);
    }

}
