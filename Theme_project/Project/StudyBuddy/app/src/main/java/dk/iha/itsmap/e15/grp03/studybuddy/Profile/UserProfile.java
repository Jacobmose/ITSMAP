package dk.iha.itsmap.e15.grp03.studybuddy.Profile;

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
    byte[] _picture;
    String _parseid;


    public UserProfile() {

    }

    public UserProfile(String name, String email, String age, String gender, String ratingAmount, String rating, byte[] picture, String parseId) {

        this._name = name;
        this._email = email;
        this._age = age;
        this._gender = gender;
        this._ratingAmount = ratingAmount;
        this._rating = rating;
        this._picture = picture;
        this._parseid = parseId;
    }

    public UserProfile(String name, String email, String age, String gender, String ratingAmount, String rating, String parseId) {

        this._name = name;
        this._email = email;
        this._age = age;
        this._gender = gender;
        this._ratingAmount = ratingAmount;
        this._rating = rating;
        this._parseid = parseId;
    }

    public UserProfile(String id, String name, String email, String age, String gender, String parseId) {
        this._id = id;
        this._name = name;
        this._email = email;
        this._age = age;
        this._gender = gender;
        this._parseid = parseId;
    }

    public String getID() {
        return this._id;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getEmail() {
        return this._email;
    }

    public void setEmail(String email) {
        this._email = email;
    }

    public String getAge() {
        return this._age;
    }

    public void setAge(String age) {
        this._age = age;
    }

    public String getGender() {
        return this._gender;
    }

    public void setGender(String gender) {
        this._gender = gender;
    }

    public String getRatingAmount() {
        return this._ratingAmount;
    }

    public void setRatingAmount(String ratingAmount) {
        this._ratingAmount = ratingAmount;
    }

    public String getRating() {
        return this._rating;
    }

    public void setRating(String rating) {
        this._rating = rating;
    }

    public String getParseId() {
        return _parseid;
    }

    public void setParseId(String parseId) {
        this._parseid = parseId;
    }
}

