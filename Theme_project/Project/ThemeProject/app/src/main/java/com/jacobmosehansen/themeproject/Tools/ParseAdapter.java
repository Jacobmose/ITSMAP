package com.jacobmosehansen.themeproject.Tools;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Andersen on 09-10-2015.
 */
public class ParseAdapter {


    public static final String KEY_AGE = "age";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_SUBJECTS = "subjects";
    public static final String KEY_PICTURE = "profilepicture";

    public static final String KEY_USERID = "userid";
    public static final String KEY_TOTALRATING = "totalrating";
    public static final String KEY_NUMBEROFRATINGS = "numberofratings";


    public ParseUser createParseUser(String username, String age, String gender, String email, String password){
        ParseUser parseUser = new ParseUser();

        parseUser.setEmail(email);
        parseUser.setUsername(username);
        parseUser.setPassword(password);
        parseUser.put(KEY_AGE, age);
        parseUser.put(KEY_GENDER, gender);

        return parseUser;
    }

    public ParseObject createParseRatingObject(String userid, double totalrating, Integer numberofratings){
        ParseObject parseObject = new ParseObject("ratingObject");

        parseObject.put(KEY_USERID, userid);
        parseObject.put(KEY_TOTALRATING, totalrating);
        parseObject.put(KEY_NUMBEROFRATINGS, numberofratings);

        return parseObject;
    }

}
