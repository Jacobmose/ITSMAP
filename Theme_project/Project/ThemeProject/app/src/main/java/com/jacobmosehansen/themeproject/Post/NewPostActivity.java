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
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by Marlene on 08-10-2015.
 */
    //Gemme posten ned i Parse
    //Koble profil og posts

public class NewPostActivity extends AppCompatActivity {

    String headline;
    Spinner subjects;
    String postText;
    ParseObject object;
    String userId;
    ParseUser userProfile = ParseUser.getCurrentUser();
    ArrayList<String> subjectArray = new ArrayList<String>();
    private ArrayAdapter<String> mySubjectAdapter;
    ArrayList<String> topics;
    ListView topicList;
    Button btnAddSubject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        object = new ParseObject("ParsePost");
        mySubjectAdapter = new ArrayAdapter<String>(NewPostActivity.this, android.R.layout.simple_list_item_1, subjectArray);
        topicList = (ListView) findViewById(R.id.listView2);
        headline = (findViewById(R.id.headLine)).toString();
        subjects = (Spinner) findViewById(R.id.spinner2);
        postText = (findViewById(R.id.postText).toString());
        //userId = ParseUser.getCurrentUser().getObjectId();
        btnAddSubject = (Button) findViewById(R.id.btn_AddSubject);

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
    }

    protected void onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View myFragmentView = inflater.inflate(R.layout.activity_new_post, container, false);

        Button button = (Button) myFragmentView.findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //fragment.finish();??
            }});

        Button button1 = (Button) myFragmentView.findViewById(R.id.btn_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send data to database
                putDataIntoParse(headline, topics, postText);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickCancel(){this.finish();}

    public void putDataIntoParse(String Headline, ArrayList<String> topics, String post){

        // Put field values into the object
        object.put("UserID",userProfile.getUsername());
        object.put("Headline",Headline);
        object.put("Topic", topics);
        object.put("PostText", post);

        // Save new post into database
        object.saveInBackground();
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
