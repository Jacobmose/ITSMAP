package com.jacobmosehansen.themeproject.Tools;

import com.parse.ParseUser;

/**
 * Created by Andersen on 09-10-2015.
 */
public class ParseAdapter {


    public static final String KEY_AGE = "age";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_RATINGAMOUNT = "rating_amount";
    public static final String KEY_RATING = "rating";
    public static final String KEY_SUBJECTS = "subjects";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_PICTURE = "profilepicture";


    public ParseUser createParseUser(String username, String age, String gender, String email, String password){
        ParseUser parseUser = new ParseUser();

        parseUser.setEmail(email);
        parseUser.setUsername(username);
        parseUser.setPassword(password);
        parseUser.put(KEY_AGE, age);
        parseUser.put(KEY_GENDER, gender);
        parseUser.put(KEY_RATING, 0);
        parseUser.put(KEY_RATINGAMOUNT, 0.0);

        return parseUser;
    }

}
