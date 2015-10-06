package com.jacobmosehansen.themeproject.Chat;

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
 * Created by Andersen on 06-10-2015.
 */
public class ChatListAdaptor extends BaseAdapter {

    Context context;
    ArrayList<ChatItem> chatItems;
    ChatItem chatItem = null;
    RoundImage roundImage;

    public ChatListAdaptor(Context context, ArrayList<ChatItem> chatItems){
        this.chatItems = chatItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chatItems.size();
    }

    @Override
    public Object getItem(int pos){
        return chatItems.get(pos);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chat_list_items, null);
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

        return convertView;
    }

}
