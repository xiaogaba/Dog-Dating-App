package com.example.xin.firex;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageManager {

    private Context context;

    public ImageManager(Context context) {
        this.context = context;
    }

    // overloaded for User Profile pic
    public Bitmap loadFromStorage(String path) {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            Toast t = Toast.makeText(context, "Load Success", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
            return b;
        }
        catch (FileNotFoundException e)
        {
            Toast t = Toast.makeText(context, "Error - file not found", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
            e.printStackTrace();
            return null;
        }
    }

    // overloaded for Dog profile pics
    public Bitmap loadFromStorage(String path, String dogName) {
        String fileName = dogName + ".jpg";
        try {
            File f=new File(path, fileName);
            String s = f.getParentFile().getPath();
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            Toast t = Toast.makeText(context, "Load Success", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
            return b;
        }
        catch (FileNotFoundException e)
        {
            Toast t = Toast.makeText(context, "Error - file not found", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER,0,0);
            t.show();
            e.printStackTrace();
            return null;
        }
    }

    // overloaded for User Profile pic
    public String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");
        String picPath = mypath.getParentFile().getPath();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Toast.makeText(context, "Profile Pic saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    // overloaded for Dog profile pic
    public String saveToInternalStorage(Bitmap bitmapImage, String dogName){
        String fileName = dogName + ".jpg";
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, fileName);
        String picPath = mypath.getParentFile().getPath();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Toast.makeText(context, fileName + " saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}
