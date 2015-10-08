package com.jacobmosehansen.themeproject.Post.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.jacobmosehansen.themeproject.Post.PostListUtil.Post;
import com.jacobmosehansen.themeproject.R;

import java.util.ArrayList;

/**
 * Created by Marlene on 08-10-2015.
 * Med inspiration fra FragmentsArnieMovies
 */
public class PostListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Post> posts;
    Post post = null;

    public PostListAdapter(Context c, ArrayList<Post> postList){
        posts = postList;
        context = c;
    }

    @Override
    public int getCount() {
        if (post!=null){
            return posts.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (posts!=null){
            return posts.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater postsInflator = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = postsInflator.inflate(R.layout.post_list_items, null);
        }

        post = posts.get(position);
        if (posts!=null){
            TextView txtNavn = (TextView) convertView.findViewById(R.id.textView9);
            txtNavn.setText(post.getAuthor());

            TextView txtOverskrift = (TextView) convertView.findViewById(R.id.textView10);
            txtOverskrift.setText(post.getOverskrift());

            TextView txtNiveau = (TextView) convertView.findViewById(R.id.textView11);
            txtNiveau.setText(post.getNiveau());

            TextView txtFag = (TextView) convertView.findViewById(R.id.textView12);
            txtFag.setText(post.getFag());

            EditText txtPost = (EditText) convertView.findViewById(R.id.editText4);
            txtPost.setText(post.getPost());

        }
        return convertView;
    }
}
