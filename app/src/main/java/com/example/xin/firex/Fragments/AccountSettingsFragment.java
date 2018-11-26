package com.example.xin.firex.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.firex.ImageManager;
import com.example.xin.firex.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class AccountSettingsFragment extends Fragment implements Spinner.OnItemSelectedListener {

    RelativeLayout baseFrame;
    ImageView profilePic;
    Spinner userStatusSpinner, removeDogSpinner;
    Button changeProfilePic, removeDog, confirmRemoveDog, cancelRemoveDog;
    TextView tvNoDogs;

    ArrayAdapter<String> spinnerAdapter;

    ArrayList<String> dogs;
    int userStatus = 0;
    int indexOfDogToRemove = 0;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_REQUEST = 1999;

    public AccountSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_settings, container, false);

        baseFrame = view.findViewById(R.id.rootLayout);
        profilePic = view.findViewById(R.id.settingsProfilePic);
        changeProfilePic = view.findViewById(R.id.buttonChangeProfilePic);
        userStatusSpinner = view.findViewById(R.id.userStatusSpinner);
        removeDog = view.findViewById(R.id.buttonRemoveDog);
        removeDogSpinner = view.findViewById(R.id.removeDogSpinner);
        confirmRemoveDog = view.findViewById(R.id.buttonConfirmRemoveDog);
        cancelRemoveDog = view.findViewById(R.id.buttonCancelRemoveDog);
        tvNoDogs = view.findViewById(R.id.tvNoDogs);

        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Set Your Profile Picture");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Take a Picture",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Choose from Gallery",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GALLERY_REQUEST);
                            }
                        });
                alertDialog.show();
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Set Your Profile Picture");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Take a Picture",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Choose from Gallery",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GALLERY_REQUEST);
                            }
                        });
                alertDialog.show();
            }
        });

        // setup User Status spinner
        userStatusSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.user_status_arr, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userStatusSpinner.setAdapter(adapter);



        removeDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDog.setVisibility(View.GONE);
                // setup Remove Dog spinner
                updateDogData();
                setupRemoveDogSpinner();
            }
        });
        confirmRemoveDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(getContext(), "Removing " + dogs.get(indexOfDogToRemove)
                        + " from your profile", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
                dogs.remove(indexOfDogToRemove);
                /*
                    TODO: DB call to remove dog from User's profile
                 */

                spinnerAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, dogs);

                removeDogSpinner.setVisibility(View.INVISIBLE);
                confirmRemoveDog.setVisibility(View.INVISIBLE);
                cancelRemoveDog.setVisibility(View.INVISIBLE);
                removeDog.setVisibility(View.VISIBLE);
            }
        });
        cancelRemoveDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDogSpinner.setVisibility(View.INVISIBLE);
                confirmRemoveDog.setVisibility(View.INVISIBLE);
                cancelRemoveDog.setVisibility(View.INVISIBLE);
                tvNoDogs.setVisibility(View.INVISIBLE);
                removeDog.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // user status spinner
        if(parent.getId() == R.id.userStatusSpinner) {
            userStatus = pos;
            /*
                TODO: Update user status in DB
                0 = Active      1 = Inactive
             */
        }
        // remove dog spinner
        else if(parent.getId() == R.id.removeDogSpinner) {
            indexOfDogToRemove = pos;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void updateDogData() {
        /*
            TODO: DB Call to get list of User's Dogs Names as Strings
         */
        dogs = new ArrayList<>();
        dogs.add("Taco");
        dogs.add("Barry");
    }

    public void setupRemoveDogSpinner() {
        if(dogs.size() > 0) {
            removeDogSpinner.setOnItemSelectedListener(this);
            spinnerAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, dogs);
            removeDogSpinner.setAdapter(spinnerAdapter);
            removeDogSpinner.setVisibility(View.VISIBLE);
            confirmRemoveDog.setVisibility(View.VISIBLE);
            cancelRemoveDog.setVisibility(View.VISIBLE);
        }
        else {
            removeDog.setVisibility(View.INVISIBLE);
            tvNoDogs.setVisibility(View.VISIBLE);
            cancelRemoveDog.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap photo;
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            profilePic.setImageBitmap(photo);
        }

        if(requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                profilePic.setImageBitmap(photo);
                /*
                    TODO: save profile pic to local storage / DB
                 */
                ImageManager im = new ImageManager(getContext());
                im.saveToInternalStorage(photo);
            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), "Error saving file", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
