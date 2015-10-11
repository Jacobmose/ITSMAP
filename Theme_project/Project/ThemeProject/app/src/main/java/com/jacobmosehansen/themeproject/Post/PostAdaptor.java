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


        /*if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.post_list, null);
        }
        chatItem = chatItems.get(position);
        if(chatItem != null){
            TextView txtTopic = (TextView) convertView.findViewById(R.id.txtChatListItemTopic);
            TextView txtPerson = (TextView) convertView.findViewById(R.id.txtChatListItemPerson);
            ImageView imgPerson = (ImageView) convertView.findViewById(R.id.imvChatListItem);

            txtTopic.setText(chatItem.getTopic());
            txtPerson.setText(chatItem.getPerson());

            if(chatItem.getPersonImg() != null){
                imgPerson.setImageDrawable(chatItem.getPersonImg());
            }
            else{
                Bitmap bm = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.default_profile);
                roundImage = new RoundImage(bm);
                imgPerson.setImageDrawable(roundImage);
            }
        }

        return convertView;*/

        post = posts.get(position);
        if(posts!=null){
            TextView txtName = (TextView) convertView.findViewById(R.id.name);
            txtName.setText(post.getName());
        }
        return convertView;
    }
}
