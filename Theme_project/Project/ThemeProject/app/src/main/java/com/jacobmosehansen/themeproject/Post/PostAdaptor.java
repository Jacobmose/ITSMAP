package com.jacobmosehansen.themeproject.Post;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.RoundImage;

import java.util.ArrayList;

/**
 * Created by Marlene on 10-10-2015.
 */
class PostAdaptor extends BaseAdapter {

    Context _context;
    ArrayList<Post> _postItems;
    Post _postItem = null;
    RoundImage roundImage;


    public PostAdaptor(Context context, ArrayList<Post> postItems){
        this._postItems = postItems;
        this._context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        if (convertView == null){
            LayoutInflater mInflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.post_list_items, null);
        }

        _postItem = _postItems.get(position);

        if (_postItem != null){
            ImageView imgPost = (ImageView) convertView.findViewById(R.id.ivImgPost);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);
            TextView tvLevel = (TextView) convertView.findViewById(R.id.tvLevel);

            tvTitle.setText(_postItem.getTitle());
            tvName.setText(_postItem.getName());
            tvSubject.setText(_postItem.getSubject());
            tvLevel.setText(_postItem.getLevel());

            if (_postItem.getPostImg() != null) {
                imgPost.setImageDrawable(_postItem.getPostImg());
            }
            else {
                Bitmap bm = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.default_profile);
                roundImage = new RoundImage(bm);
                imgPost.setImageDrawable(roundImage);
            }
        }

        return convertView;
    }


    @Override
    public int getCount() {
        if (_postItems!=null) {
            return _postItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (_postItems!=null){
            return _postItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
