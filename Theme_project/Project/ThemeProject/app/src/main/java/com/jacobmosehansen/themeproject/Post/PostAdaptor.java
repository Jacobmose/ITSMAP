package com.jacobmosehansen.themeproject.Post;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jacobmosehansen.themeproject.R;

import java.util.ArrayList;

/**
 * Created by Marlene on 10-10-2015.
 */
class PostAdaptor extends BaseAdapter{

    Context context;
    ArrayList<Post> posts;
    Post post = null;

    public PostAdaptor(Context c, ArrayList<Post> postList){
        posts = postList;
        context = c;
    }

    @Override
    public int getCount() {
        if (posts!=null) {
            return posts.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (posts!=null){
            return posts.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        post = posts.get(position);
        if(posts!=null){
            TextView txtName = (TextView) convertView.findViewById(R.id.name);
            txtName.setText(post.getName());
        }
        return convertView;
    }
}
