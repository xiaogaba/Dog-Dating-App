package com.example.xin.firex.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.xin.firex.ImageManager;
import com.example.xin.firex.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class AddDogFragment extends android.support.v4.app.Fragment {

    EditText addDogEditName, addDogBreed;

    // Birthday Date Picker
    EditText chooseBirthday;
    DatePickerDialog picker;
    int month, day, year;

    // RadioButtons
    RadioGroup radioGender, radioSize;
    int genderSelectedRadioId = -1; // 0 = male  1 = female
    int sizeSelectedRadioId = -1;   // 0 = small 1 = medium 2 = large

    // Dog Pic
    ImageView dogPic;
    private String mCurrentPhotoPath;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_REQUEST = 1999;

    Button savePic, loadPic;
    String picPath, dogName;

    public AddDogFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_dog, container, false);
        bindViews(view);


        return view;
    }

    private void bindViews(View view) {
        addDogEditName = view.findViewById(R.id.addDogEditName);
        addDogBreed = view.findViewById(R.id.addDogEditBreed);

        /*
            BIRTHDAY DATE PICKER
         */
        chooseBirthday = view.findViewById(R.id.addDogEditBirthday);

        chooseBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                chooseBirthday.setText((monthOfYear + 1) + "/" + dayOfMonth  + "/" + year);
                                extractDate();
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        /*
            Radio buttons
         */
        radioGender = view.findViewById(R.id.addDogRadioGroupGender);
        radioSize = view.findViewById(R.id.addDogRadioGroupSize);

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGender.findViewById(checkedId);
                genderSelectedRadioId = radioGender.indexOfChild(radioButton);
            }
        });
        radioSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioSize.findViewById(checkedId);
                sizeSelectedRadioId = radioSize.indexOfChild(radioButton);
            }
        });

        /*
            Dog Pic Choosing
         */
        dogPic = view.findViewById(R.id.addDogProfilePic);

        dogPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Set Dog's Picture");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Take a Picture",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* asking for camera permissions - PERMISSION IS ALWAYS DENIED
                                if (ContextCompat.checkSelfPermission(AddDogActivity.this, Manifest.permission.CAMERA)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(AddDogActivity.this,
                                            new String[]{Manifest.permission.CAMERA},
                                            MY_CAMERA_PERMISSION_CODE);
                                } else {
                                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                }
                                */
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
        savePic = view.findViewById(R.id.savePic);
        loadPic = view.findViewById(R.id.loadPic);

        savePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dogName = addDogEditName.getText().toString();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) dogPic.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ImageManager im = new ImageManager(getContext());
                picPath = im.saveToInternalStorage(bitmap, dogName);
                dogPic.setImageDrawable(null);
            }
        });

        loadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dogName = addDogEditName.getText().toString();
                ImageManager im = new ImageManager(getContext());
                Bitmap b = im.loadFromStorage(picPath, dogName);
                if(b == null) {
                    Toast t = Toast.makeText(getContext(), "Bitmap null", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
                else
                    dogPic.setImageBitmap(b);
            }
        });
    }

    // Birthday DatePicker accessory method
    public void extractDate() {
        String[] date = chooseBirthday.getText().toString().split("/");
        month = Integer.parseInt(date[0]);
        day = Integer.parseInt(date[1]);
        year = Integer.parseInt(date[2]);
        Toast.makeText(getContext(), "Birthday: " + month + "/" + day + "/" + year, Toast.LENGTH_SHORT).show();
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
            dogPic.setImageBitmap(photo);
        }

        if(requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                dogPic.setImageBitmap(photo);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}