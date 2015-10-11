package com.jacobmosehansen.themeproject.Chat;

import android.app.Activity;
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

    private ArrayList<ChatItem> chatItems;
    RoundImage roundImage;
    private LayoutInflater layoutInflater;

    public ChatListAdaptor(Activity activity) {
        layoutInflater = activity.getLayoutInflater();
        chatItems = new ArrayList<>();
    }

    public void addMessage(ChatItem item) {
        chatItems.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return chatItems.size();
    }

    @Override
    public ChatItem getItem(int i){
        return chatItems.get(i);
    }

    public ArrayList<ChatItem> getChatItems(){
        return chatItems;
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent){

        ChatItem chatItem = chatItems.get(i);

        if (convertView == null){

            convertView = layoutInflater.inflate(R.layout.chat_list_items, null);

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


        }
        return convertView;
    }

}
