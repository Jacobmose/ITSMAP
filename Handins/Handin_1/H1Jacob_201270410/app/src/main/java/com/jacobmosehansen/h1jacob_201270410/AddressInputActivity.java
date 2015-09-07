package com.jacobmosehansen.h1jacob_201270410;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddressInputActivity extends AppCompatActivity {

    Button btn_accept_details, btn_back;
    EditText et_road_name, et_house_number, et_post_code, et_city_name;
    int house_number_int_value = 0, post_code_int_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_input);

        btn_accept_details = (Button) findViewById(R.id.accept_address_details_btn_address_input);
        btn_back = (Button) findViewById(R.id.btn_back_activity_address_input);
        et_road_name = (EditText) findViewById(R.id.road_name);
        et_house_number = (EditText) findViewById(R.id.house_number);
        et_post_code = (EditText) findViewById(R.id.post_code);
        et_city_name = (EditText) findViewById(R.id.city_name);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressInputActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

            btn_accept_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String house_number_et_value = et_house_number.getText().toString();
                    String post_code_et_value = et_post_code.getText().toString();

                    try {
                        house_number_int_value = Integer.valueOf(house_number_et_value);
                        post_code_int_value = Integer.valueOf(post_code_et_value);
                    }
                    catch (NumberFormatException nfe){}

                    Intent intent = new Intent(AddressInputActivity.this, MainActivity.class);
                    intent.putExtra("string_et_road_name", et_road_name.getText().toString());
                    intent.putExtra("string_et_house_number", house_number_int_value);
                    intent.putExtra("string_et_post_code", post_code_int_value);
                    intent.putExtra("string_et_city_name", et_city_name.getText().toString());

                    startActivity(intent);
                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address_input, menu);
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
