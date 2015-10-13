package dk.iha.itsmap.e15.grp03.studybuddy.Post;

import com.parse.ParseObject;

/**
 * Created by Jacobmosehansen on 11-10-2015.
 */
public interface PostInterface {

    public void onPostSelected(int pos);
    public ParseObject onGetSelectedTopic();
}
