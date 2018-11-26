package com.example.xin.firex;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Gravity;
import android.widget.Toast;

public class SQLiteManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DoggiePlaydate";
    private static final int DATABASE_VERSION = 1;
    private static int playdateCount = 0;

    public SQLiteManager (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS Playdates(" +
                "_id integer primary key autoincrement," +
                "date text, " +
                "latitude real, " +
                "longitude real)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
        String sql = "DROP TABLE IF EXISTS Playdates;";
        db.execSQL(sql);
        onCreate(db);
    }

    public void addPlaydate(Playdate pd, Context con) {


        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("date", pd.dateToDBString());
        cv.put("latitude", pd.meetingPlace.getLatitude());
        cv.put("longitude", pd.meetingPlace.getLongitude());
        int lastIndex = (int) db.insert("Playdates", null, cv);
/*
        // get index of last row to correctly create PDx table holding users
        String query = "SELECT max(_id) from Playdates";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int index = c.getInt(c.getColumnIndexOrThrow("max(_id)"));
  */
        Toast t = Toast.makeText(con, "index of last row in playdates: " + lastIndex, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();

        // create table PDx to hold list of attendees
        String sql = "CREATE TABLE IF NOT EXISTS PD" + lastIndex + "(" +
                "pdId integer, " +
                "user text, " +
                "FOREIGN KEY (pdId) REFERENCES Playdates(_id), " +
                "PRIMARY KEY (pdId, user));";
        db.execSQL(sql);

        for(int x = 0; x < pd.attendees.size(); x++) {
            ContentValues cv1 = new ContentValues();
            cv1.put("pdId", lastIndex);
            cv1.put("user", pd.attendees.get(x));
            int success = (int) db.insert("PD" + lastIndex, null, cv1);
        }

    }

    public boolean deletePlaydate(int pdId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Playdates", "_id="+pdId, null);
        return true;
    }

    public boolean deletePlaydateTable(int pdId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS PD" + pdId);
        return true;
    }

    public Cursor getAllPlaydates() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM Playdates ORDER BY date ASC", null);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public Cursor getPlaydatesAfter(String todaysDateDBString) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM Playdates WHERE date>=" + todaysDateDBString + " ORDER BY date(date) ASC", null);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Cursor getSinglePlaydate(int pdId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM PD" + pdId, null);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
