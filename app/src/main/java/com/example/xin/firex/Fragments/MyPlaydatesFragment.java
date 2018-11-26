package com.example.xin.firex.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.xin.firex.Playdate;
import com.example.xin.firex.PlaydatesAdapter;
import com.example.xin.firex.R;
import com.example.xin.firex.SQLiteManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyPlaydatesFragment extends Fragment {

    ListView lv;
    FloatingActionButton fabAddPlaydate;

    ArrayAdapter<Playdate> adapter;

    PlaydateListener mCallback;

    public MyPlaydatesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myplaydates, container, false);
        bindViews(view);
        setAndAttachLVAdapter();

        return view;
    }

    private void bindViews(View view) {
        lv = view.findViewById(R.id.lvPlaydates);
        fabAddPlaydate = view.findViewById(R.id.fabNewPlaydate);
        fabAddPlaydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.playdatesFabGoToCreatePlaydateFragment();
            }
        });
    }

    private void setAndAttachLVAdapter() {
        ArrayList<Playdate> myPlaydates = new ArrayList<>();
        ArrayList<String> attendees = new ArrayList<>();

        SQLiteManager dbManager = new SQLiteManager(getContext());
        // get Playdates for days >= today
        Date todaysDate = Calendar.getInstance().getTime();
        SimpleDateFormat myFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String todaysString = myFormat1.format(todaysDate);

        Cursor cursor = dbManager.getPlaydatesAfter(todaysString);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                int x = 1;

                do {
                    attendees = new ArrayList<>();
                    // get individual Playdate table, create playdates list
                    Cursor pdCursor = dbManager.getSinglePlaydate(x);
                    if(pdCursor != null) {
                        if (pdCursor.moveToFirst()) {
                            do {
                                attendees.add(pdCursor.getString(pdCursor.getColumnIndexOrThrow("user")));
                            } while (pdCursor.moveToNext());

                            // build Playdate object from table read-in
                            String dateString = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date date = new Date();
                            try {
                                date = myFormat.parse(dateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            double lat = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                            double lon = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                            Location loc = new Location("");
                            loc.setLatitude(lat);
                            loc.setLongitude(lon);
                            Playdate pd = new Playdate(attendees, date, loc);
                            myPlaydates.add(pd);
                            x++;
                        }
                    }
                } while(cursor.moveToNext());
            }
        }
        else {
            /*
            FOR DATE:
                month is 0 indexed  - 0 = January
                year is 1900 as base - For 2016, input 116 for year
            */
            attendees.add("abc");
            attendees.add("def");
            Date date = new Date(118, 1, 1, 15, 30);
            Location meetingPlace = new Location("");
            meetingPlace.setLatitude(37.3382);
            meetingPlace.setLongitude(-121.8863);
            myPlaydates.add(new Playdate(attendees, date, meetingPlace));
            date = new Date(118, 11, 30, 8, 15);
            myPlaydates.add(new Playdate(attendees, date, meetingPlace));
        }


        adapter = new PlaydatesAdapter(getActivity(), myPlaydates);
        lv.setAdapter(adapter);
    }

    // Ensures that Activity has implemented FiltersFragmentListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlaydateListener) {
            mCallback = (PlaydateListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FiltersFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface PlaydateListener {
        void playdatesFabGoToCreatePlaydateFragment();
    }

}