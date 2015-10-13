package com.jacobmosehansen.themeproject.Post;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Jacobmosehansen on 11-10-2015.
 */
public interface PostInterface {

    public void onPostSelected(int pos);
    public ParseObject onGetSelectedTopic();
}
