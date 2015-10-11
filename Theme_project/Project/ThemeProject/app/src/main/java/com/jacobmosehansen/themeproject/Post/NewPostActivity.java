package com.jacobmosehansen.themeproject.Post;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jacobmosehansen.themeproject.R;
/**
 * Created by Marlene on 08-10-2015.
 */
    //Gemme posten ned i Parse
    //Koble profil og posts

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
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

    public void onClickSubmit(String Headline, String level, String course, String post){
        //Gemme ned i databasen
    }

    /*public void selectFromSpinner(){
        try{
            String selectedSubject = sprSubjects.getSelectedItem().toString();

            if(subjectArray.size() < 5){
                if(subjectArray.contains(selectedSubject)){
                    Toast.makeText(getActivity(), "Subject already added", Toast.LENGTH_SHORT).show();
                }else {
                    mySubjectAdapter.add(selectedSubject);
                    lvSubjects.setAdapter(mySubjectAdapter);
                    //_TODO SAVE SUBJECTS TO DATABASE //
                }}
            else{Toast.makeText(getActivity(), "Too many subjects added", Toast.LENGTH_SHORT).show();}
        }catch(Exception e){
            Toast.makeText(getActivity(), "No subject selected", Toast.LENGTH_SHORT).show();
        }
    }*/
}
