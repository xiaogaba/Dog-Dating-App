package com.example.xin.firex.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.xin.firex.Playdate;
import com.example.xin.firex.R;
import com.example.xin.firex.SQLiteManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreatePlaydateFragment extends Fragment {

    Button buttonInvite, buttonCreatePlaydate;
    EditText cpInviteList, chooseDate, chooseTime;
    Spinner filterSpinner;

    ArrayAdapter<CharSequence> filterSpinnerAdapter;
    ArrayList<String> userEmailList, userNameList, inviteList;  // invite list used for backend Playdate creation
    ArrayList<Float> userDistanceList;
    int indexFilterSelection = 0,      // 0 = 1 mile, 1 = 2 mile, 2 = 5 mile
            indexUserSelection = -1;

    DatePickerDialog datePicker;
    int dmonth= -1, dday = -1, dyear = -1;
    TimePickerDialog timePicker;
    int thour = -1, tminute = -1;

    Location meetingLocation;    // String meetingAddress ?

    AfterCreatePlaydate mCallback;

    public CreatePlaydateFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_playdate, container, false);
        bindViews(view);


        return view;
    }

    private void bindViews(View view) {
        buttonInvite = view.findViewById(R.id.cpInvite);
        buttonCreatePlaydate = view.findViewById(R.id.cpButtonCreatePlaydate);
        cpInviteList = view.findViewById(R.id.cpInviteList);
        filterSpinner = view.findViewById(R.id.cpFilterSpinner);
        chooseDate = view.findViewById(R.id.cpETDate);
        chooseTime = view.findViewById(R.id.cpETTime);

        userEmailList = new ArrayList<>();
        userNameList = new ArrayList<>();

        /*
            TODO: placeholder data
         */
        userNameList.add("Bob");
        userNameList.add("Sally");
        userNameList.add("Frank");
        userNameList.add("Betty");
        userEmailList.add("a@a.com");
        userEmailList.add("b@b.com");
        userEmailList.add("c@c.com");
        userEmailList.add("d@d.com");

        userDistanceList = new ArrayList<>();
        inviteList = new ArrayList<>();

        // set up buttonInvite
        buttonInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUsers(indexFilterSelection);
            }
        });

        // set up buttonCreatePlaydate
        buttonCreatePlaydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPlaydate();
            }
        });

        // set up Filter-by distance spinner
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexFilterSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        filterSpinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.filter_by_arr,
                                                              android.R.layout.simple_spinner_item);
        filterSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterSpinnerAdapter);


        // set up date picker
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                datePicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                chooseDate.setText((monthOfYear + 1) + "/" + dayOfMonth  + "/" + year);
                                extractDate(year, monthOfYear, dayOfMonth);
                            }
                        }, year, month, day);
                datePicker.setTitle("Choose a Date");
                datePicker.show();
            }
        });
        // set up time picker
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        chooseTime.setText(hourOfDay + ":" + minute);
                        extractTime(hourOfDay, minute);
                    }
                }, hour, minute, true);
                timePicker.setTitle("Pick a Time");
                timePicker.show();
            }
        });
    }

    // DatePicker accessory method
    public void extractDate(int year, int month, int day) {
        dyear = year;
        dmonth = month + 1;
        dday = day;
        Toast.makeText(getContext(), "Date: " + dmonth + "/" + dday + "/" + dyear, Toast.LENGTH_SHORT).show();
    }

    // TimePicker accessory method
    public void extractTime(int hour, int minute) {
        thour = hour;
        tminute = minute;
        Toast.makeText(getContext(), "Time: " + thour + ":" + tminute, Toast.LENGTH_SHORT).show();
    }

    public void showUsers(int indexFilterSelection) {
        // Show dialog box w/ list of users, FILTERS APPLIED

        /*
            TODO: DB call to get list of users, apply filter
            indexFilterSelection     0 = 1 mile, 1 = 2 miles, 2 = 5 miles
            save user emails in userEmailList (string vals) - EMAIL FOR BACKEND
            save user names in userNameList (string vals) - display in UI
            save user distances in userDistanceList (float vals) - display in UI

         */


        AlertDialog.Builder chooseUserDialog = new AlertDialog.Builder(getContext());
        chooseUserDialog.setTitle("Invite: ");

        // CAN USE CURSOR W/ ARRAYDAPTER FOR DB
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice, userNameList);


        chooseUserDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                dialog.dismiss();
            }
        });

        chooseUserDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                addUserToPlaydate(index);
            }
        });
        chooseUserDialog.show();
    }

    private void addUserToPlaydate(int index) {
        String userEmail = userEmailList.get(index);
        String userName = userNameList.get(index);

        cpInviteList.setText(cpInviteList.getText().toString() + userName + " ");
        // add to backend list to create Playdate
        inviteList.add(userEmail);
    }

    private void createPlaydate() {
        // create meetingLocation from LOCATION SEARCH BAR
        meetingLocation = new Location("");
        meetingLocation.setLatitude(37.3503);
        meetingLocation.setLongitude(-121.9607);

        // check for empty fields
        if(dyear == -1 || thour == -1 || inviteList.size() == 0  || meetingLocation == null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Error");
            alert.setMessage("You must fill out all fields to continue");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        else {
            Date dt = new Date(dyear-1900, dmonth, dday, thour, tminute);

            Playdate pd = new Playdate(inviteList, dt, meetingLocation);
            /*
                TODO: save playdate to local storage DB (sqlite)
                    add playdate to local calendar app
                    send notification to other users on playdate to schedule
                    add playdate to other users local calendar app
             */
            savePlaydateToSQLiteDB(pd);
            navToMyPlaydates();
        }
    }

    private void savePlaydateToSQLiteDB(Playdate pd) {
        SQLiteManager dbManager = new SQLiteManager(getContext());
        dbManager.addPlaydate(pd, getContext());
    }

    private void navToMyPlaydates() {
        mCallback.goToMyPlaydates();
    }

    // Ensures that Activity has implemented FiltersFragmentListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreatePlaydateFragment.AfterCreatePlaydate) {
            mCallback = (CreatePlaydateFragment.AfterCreatePlaydate) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AfterCreatePlaydate");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface AfterCreatePlaydate {
        void goToMyPlaydates();
    }

}