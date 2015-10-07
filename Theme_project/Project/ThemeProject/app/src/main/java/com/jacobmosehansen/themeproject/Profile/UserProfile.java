package com.jacobmosehansen.themeproject.Profile;

/**
 * Created by Jacobmosehansen on 07-10-2015.
 */
public class UserProfile {

    String _id;
    String _name;
    String _age;
    String _gender;
    String _email;
    String _ratingAmount;
    String _rating;

    public UserProfile(){

    }

    public UserProfile(String name, String age, String gender, String ratingAmount, String rating){
        this._name = name;
        this._age = age;
        this._gender = gender;
        this._ratingAmount = ratingAmount;
        this._rating = rating;
    }

    public UserProfile(String id, String name, String age, String gender){
        this._id = id;
        this._name = name;
        this._age = age;
        this._gender = gender;
    }

    public String getID(){
        return this._id;
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public String getAge(){
        return this._age;
    }

    public void setAge(String age){
        this._age = age;
    }

    public String getGender(){
        return this._gender;
    }

    public void setGender(String gender){
        this._gender = gender;
    }

    public String getRatingAmount(){
        return this._ratingAmount;
    }

    public void setRatingAmount(String ratingAmount){
        this._ratingAmount = ratingAmount;
    }

    public String getRating(){
        return this._rating;
    }

    public void setRating(String rating){
        this._rating = rating;
    }
}
