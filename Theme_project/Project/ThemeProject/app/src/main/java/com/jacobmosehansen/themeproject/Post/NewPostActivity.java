package com.jacobmosehansen.themeproject.Post;

import android.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.NothingSelectedSpinnerAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by Marlene on 08-10-2015.
 */
    //Gemme posten ned i Parse
    //Koble profil og posts

public class NewPostActivity extends AppCompatActivity {

    EditText headline;
    Spinner subjects;
    EditText postText;
    String userId;
    ParseUser userProfile = ParseUser.getCurrentUser();
    ArrayList<String> subjectArray = new ArrayList<String>();
    private ArrayAdapter<String> mySubjectAdapter;
    ArrayList<String> topics;
    ListView topicList;
    Button btnAddSubject;
    Button btnCancel;
    Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        mySubjectAdapter = new ArrayAdapter<String>(NewPostActivity.this, android.R.layout.simple_list_item_1, subjectArray);
        topicList = (ListView) findViewById(R.id.listView2);
        headline = (EditText) findViewById(R.id.headLine);
        subjects = (Spinner) findViewById(R.id.spinner2);
        postText = (EditText) findViewById(R.id.postText);
        userId = ParseUser.getCurrentUser().getObjectId();
        btnAddSubject = (Button) findViewById(R.id.btn_AddSubject);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        final ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(NewPostActivity.this,
                R.array.subjects_array, android.R.layout.simple_spinner_item);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set Subject Selection Preset Text //
        subjects.setAdapter(new NothingSelectedSpinnerAdapter(myAdapter,
                R.layout.spinner_row_nothing_selected, NewPostActivity.this));

        //Do on btn_addSubject add chosen subject to listView //
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFromSpinner();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (headline.getText() != null) {
                    if (subjectArray != null) {
                        if (postText.getText() != null) {
                            putDataIntoParse(headline.getText().toString(), subjectArray, postText.getText().toString());
                        } else {
                            Toast.makeText(NewPostActivity.this, "Description most include text", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(NewPostActivity.this, "No topic selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewPostActivity.this, "Post Title most include text", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onClickCancel(){this.finish();}

    public void putDataIntoParse(String Headline, ArrayList<String> topics, String post){

        // Put field values into the object
        ParseObject object = new ParseObject("ParsePost");
        object.put("UserName",userProfile.getUsername());
        object.put("UserID", userProfile.getObjectId());
        object.put("Headline",Headline);
        object.put("Topic", topics);
        object.put("PostText", post);

        // Save new post into database
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    onClickCancel();
                }else {
                    Toast.makeText(NewPostActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void selectFromSpinner(){
        try{
            String selectedSubject = subjects.getSelectedItem().toString();
            Log.d("Test", "Selected subject: " + selectedSubject);

            if(subjectArray.size() < 5){
                if(subjectArray.contains(selectedSubject)){
                    Toast.makeText(NewPostActivity.this, "Subject already added", Toast.LENGTH_SHORT).show();
                }else {
                    mySubjectAdapter.add(selectedSubject);
                    topicList.setAdapter(mySubjectAdapter);
                    //_TODO SAVE SUBJECTS TO DATABASE //
                }}
            else{Toast.makeText(NewPostActivity.this, "Too many subjects added", Toast.LENGTH_SHORT).show();}
        }catch(Exception e){
            Toast.makeText(NewPostActivity.this, "No subject selected", Toast.LENGTH_SHORT).show();
        }
    }
}
