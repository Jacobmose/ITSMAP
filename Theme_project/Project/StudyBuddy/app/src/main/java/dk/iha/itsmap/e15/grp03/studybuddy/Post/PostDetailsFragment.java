package dk.iha.itsmap.e15.grp03.studybuddy.Post;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import dk.iha.itsmap.e15.grp03.studybuddy.R;
import com.parse.ParseObject;

import java.util.ArrayList;

import dk.iha.itsmap.e15.grp03.studybuddy.Chat.ChatItem;
import dk.iha.itsmap.e15.grp03.studybuddy.Chat.ChatMessageActivity;
import dk.iha.itsmap.e15.grp03.studybuddy.Profile.ProfileActivity;
import dk.iha.itsmap.e15.grp03.studybuddy.Tools.RoundImage;

/**
 * Created by Marlene on 10-10-2015.
 */
    //Skal vise selve info'en omkring Posts.
    //Skal kunne videresende en til både profil og messenger ved at klikke på hhv. billede eller "Contact"

public class PostDetailsFragment extends Fragment{

    Button button;
    Button startChatButton;
    ParseObject currentTopic;

    TextView txt_name;
    TextView txt_HeadLine;
    EditText ed_subject;
    EditText ed_text;
    PostInterface postInterface;
    ImageView img_image;
    RoundImage roundImage;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        currentTopic = postInterface.onGetSelectedTopic();

        View myFragmentView = inflater.inflate(R.layout.fragment_show_post, container, false);
        container.removeAllViews();

        button = (Button) myFragmentView.findViewById(R.id.btn_showProfile);
        startChatButton = (Button) myFragmentView.findViewById(R.id.btn_StartChat);
        txt_name = (TextView) myFragmentView.findViewById(R.id.name);
        txt_HeadLine = (TextView) myFragmentView.findViewById(R.id.Post_Headline);
        ed_subject = (EditText) myFragmentView.findViewById(R.id.Post_note);
        ed_text =(EditText) myFragmentView.findViewById(R.id.editText3);
        img_image = (ImageView) myFragmentView.findViewById(R.id.imageView_post_show);

        Bitmap bm = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.default_profile);
        roundImage = new RoundImage(bm);

        //currentTopic = postInterface.onGetSelectedTopic();
        if(currentTopic == null){
            Log.d("ERROR", "onViewCreated");
        }else{
            setView();
        }


        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                Log.d("TEST", "test: " + currentTopic.get("UserID").toString());
                intent.putExtra("USER_ID", currentTopic.get("UserID").toString());
                startActivity(intent);
            }
        });

        startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatMessageActivity.class);
                intent.putExtra("CHATITEM", new ChatItem(currentTopic.get("UserName").toString(),
                        currentTopic.get("Headline").toString(),
                        currentTopic.get("UserID").toString(),
                        currentTopic.getObjectId()));
                startActivity(intent);
            }
        });
        return myFragmentView;
    }

    public void setView(){
        String tagString = "";
        if(currentTopic == null){
            Log.d("ERROR", "ERROR");
        }else{
            txt_name.setText(currentTopic.get("UserName").toString());
            txt_HeadLine.setText(currentTopic.get("Headline").toString());
            ed_text.setText(currentTopic.get("PostText").toString());
            ArrayList<String> tagList = (ArrayList<String>) currentTopic.get("Topic");
            for (int i = 0; i < tagList.size(); i++) {
                if (i != 0) {
                   tagString += ", ";
                }
                tagString = tagList.get(i).toString();
            }
            ed_subject.setText(tagString);
            img_image.setImageDrawable(roundImage);
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        postInterface = (PostInterface) activity;
    }
}
