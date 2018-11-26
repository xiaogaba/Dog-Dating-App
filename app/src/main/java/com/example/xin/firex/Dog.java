package com.example.xin.firex;

public class Dog {
    public int id;
    public String name;
    public String breed;
    public int gender;
    public Birthday bday;
    public String profilePicPath;
    private static int idCounter = 0;

    public Dog(String name, String breed, int gender, Birthday bday) {
        this.id = idCounter++;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.bday = bday;
    }

    public void setProfilePic(String picPath) {
        this.profilePicPath = picPath;
    }

    class Birthday {
        int month;
        int day;
        int year;

        public String toString() {
            return month + "/" + day + "/" + year;
        }
    }
}
