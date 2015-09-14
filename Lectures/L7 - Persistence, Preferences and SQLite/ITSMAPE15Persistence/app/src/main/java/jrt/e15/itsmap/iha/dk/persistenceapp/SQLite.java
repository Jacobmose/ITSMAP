package jrt.e15.itsmap.iha.dk.persistenceapp;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SQLite extends Activity {
	
	private DAO dao;
	private TextView txt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dao = new DAO(this);
        
        final EditText editText = (EditText) findViewById(R.id.editText1);
        final Button button = (Button) findViewById(R.id.button1);
		final Button button2 = (Button) findViewById(R.id.button2);
        txt = (TextView) findViewById(R.id.textView1);

        showDBContents();
        
        button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String name = editText.getText().toString();
				dao.insert(name);
				editText.setText("");

				showDBContents();
			}
		});
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dao.deleteAll();

				showDBContents();
			}
		});
	}
    
    void showDBContents() {
    	List<String> names = dao.selectAll();
		StringBuilder sb = new StringBuilder();
		for (String sname : names) { 
			sb.append(sname + "\n");  
		}
		txt.setText(sb.toString());
    }
}