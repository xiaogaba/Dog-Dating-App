package com.example.xin.firex.Model;

public class User_Dog {
    private String user_id, dog_id, dog_name, dog_pic_url, breed, gender, bday, size, medical_info, about;
    public User_Dog(){

    }
    public User_Dog(String user_id, String dog_id, String dog_name, String dog_pic_url, String breed, String gender, String bday, String size, String medical_info, String about){
        this.user_id = user_id;
        this.dog_id = dog_id;
        this.dog_name = dog_name;
        this.dog_pic_url = dog_pic_url;
        this.breed = breed;
        this.gender = gender;
        this.bday = bday;
        this.size = size;
        this.medical_info = medical_info;
        this.about = about;
    }

    public String getUser_id(){
        return user_id;
    }

    public String getDog_id(){
        return dog_id;
    }

    public void setDog_id(String dog_id) {
        this.dog_id = dog_id;
    }

    public void setDog_name(String dog_name) {
        this.dog_name = dog_name;
    }

    public String getDog_name(){
        return dog_name;
    }

    public String getDog_pic_url() {return dog_pic_url; }

    public void setBreed(String breed) {this.breed = breed; }

    public String getBreed() {return breed; }

    public void setGender(String gender) {this.gender = gender; }

    public String getGender() {return gender; }

    public void setDog_pic_url(String dog_pic_url) {this.dog_pic_url = dog_pic_url; }

    public String getBday(){
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getdog_size(){
        return size;
    }

    public void setdog_size(String size) {
        this.size = size;
    }

    public String getMedical_info(){
        return medical_info;
    }

    public void setMedical_info(String medical_info) {
        this.medical_info = medical_info;
    }

    public String getAbout(){ return about; }

    public void setAbout(String about) {
        this.about = about;
    }
}