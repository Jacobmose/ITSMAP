package com.jacobmosehansen.themeproject.Post.PostListUtil;

import java.util.ArrayList;

/**
 * Created by Marlene on 08-10-2015.
 */
public interface PostSelectorInterface {

    public void onPostSelected(int position);
    public ArrayList<Post> getPostList();
    public Post getCurrentSelection();
    public void viewSpecial();
}


