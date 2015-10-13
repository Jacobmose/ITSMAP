package dk.iha.itsmap.e15.grp03.studybuddy.Post;

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

import dk.iha.itsmap.e15.grp03.studybuddy.R;
import com.parse.ParseObject;

import java.util.ArrayList;

import dk.iha.itsmap.e15.grp03.studybuddy.Tools.RoundImage;

/**
 * Created by Marlene on 10-10-2015.
 */
class PostAdaptor extends BaseAdapter {

    Context _context;
    ArrayList<ParseObject> _postItems;
    Post _postItem = null;
    RoundImage roundImage;


    public PostAdaptor(Context context, ArrayList<ParseObject> postItems){
        this._postItems = postItems;
        this._context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        String tagString = "";
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            LayoutInflater mInflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.post_list_items, null);
            ImageView imgPost = (ImageView) convertView.findViewById(R.id.ivImgPost);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);
        }

        ParseObject _postItem = getItem(position);

        if (_postItem != null){
            ImageView imgPost = (ImageView) convertView.findViewById(R.id.ivImgPost);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            TextView tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);

            tvTitle.setText(_postItem.get("Headline").toString());
            tvName.setText("User: " + _postItem.get("UserName").toString());
            ArrayList<String> tagList = (ArrayList<String>) _postItem.get("Topic");
            for (int i = 0; i < tagList.size(); i++) {
                if (i != 0) {
                    tagString += ", ";
                }
                tagString = tagList.get(i).toString();
            }
            tvSubject.setText("Topics: " + tagString);

            /*if (_postItem.getPostImg() != null) {
                imgPost.setImageDrawable(_postItem.getPostImg());
            }
            else {*/
                Bitmap bm = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.default_profile);
                roundImage = new RoundImage(bm);
                imgPost.setImageDrawable(roundImage);
            //}
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
    public ParseObject getItem(int position) {
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
