package com.jacobmosehansen.h1jacob_201270410;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    Drawable image;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.btn_input_prompt);
        textView = (TextView)findViewById(R.id.tv_address_details_show);
        img = (ImageView)findViewById(R.id.imageView);
        Intent getIntent = getIntent();
        Bundle extras = getIntent.getExtras();

        if(extras != null) {
            String road_name = getIntent.getExtras().getString("string_et_road_name");
            String city_name = getIntent.getExtras().getString("string_et_city_name");
            int house_number = getIntent.getExtras().getInt("string_et_house_number");
            int post_code = getIntent.getExtras().getInt("string_et_post_code");

            String addressDetails = road_name + " " + house_number + ", " + post_code + " " + city_name;
            String rn = "Road Name";
            String cn = "City Name";

            try {
                if (!road_name.equals(rn) && !city_name.equals(cn)){
                    int imageResource = R.drawable.dress_blue;
                    image = getResources().getDrawable(imageResource);
                    img.setImageDrawable(image);
                    textView.setText(addressDetails);
                }
                else textView.setText("");
            }
            catch (Exception e) {}

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddressInputActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
