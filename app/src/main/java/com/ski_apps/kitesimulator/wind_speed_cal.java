package com.ski_apps.kitesimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class wind_speed_cal extends AppCompatActivity {
AutoCompleteTextView country;
TextView label, direction;
EditText zipcode;
Button submit, done;
private RequestQueue mRequestQueue;
static String wind_speed_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wind_speed_cal);
        country = findViewById(R.id.autoCompleteTextView);
        label = findViewById(R.id.label);
        zipcode = findViewById(R.id.zipcode);
        done = findViewById(R.id.btn_done);
        submit = findViewById(R.id.submit);
        direction = findViewById(R.id.textview_direction);
        mRequestQueue = Volley.newRequestQueue(this);


        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length()>0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, countries);

        country.setThreshold(2);
        country.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_wind_speed();
            }
        });
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(wind_speed_cal.this, MainActivity.class);
                    intent.putExtra("wind_speed", wind_speed_string);
                    startActivity(intent);
                }
            });
        }

    public String getCountryCode(String countryName) {

        String[] isoCountryCodes = Locale.getISOCountries();
        Map<String, String> countryMap = new HashMap<>();
        Locale locale;
        String name;

        for (String code : isoCountryCodes) {
            locale = new Locale("", code);
            name = locale.getDisplayCountry();
            countryMap.put(name, code);
        }

        return countryMap.get(countryName);
    }
    public void get_wind_speed(){

        String country_string = country.getText().toString();
        String country_code = getCountryCode(country_string);
        String zipcode_string = zipcode.getText().toString();
        Log.d("TAG", "zipcode : "+zipcode_string);
        Log.d("TAG", "country code: "+country_code);
        if(!TextUtils.isEmpty(country_string) && !(TextUtils.isEmpty(zipcode_string))){
            String url = "https://api.openweathermap.org/data/2.5/weather?zip="+zipcode_string+","+country_code+"&appid=f3e282d3fd3cfb2802f224671d15c7c4";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String response_code = response.getString("cod");
                                if(!response_code.equals("404")){
                                    JSONObject wind_obj = response.getJSONObject("wind");
                                    String wind_speed = wind_obj.getString("speed");
                                    String direction_str = wind_obj.getString("deg");
                                    wind_speed_string = wind_speed;
                                    label.setText(wind_speed);
                                    direction.setText(direction_str);

                                }else{
                                    Toast.makeText(wind_speed_cal.this, "Invalid Input", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(wind_speed_cal.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(wind_speed_cal.this, "Error, Try again!", Toast.LENGTH_SHORT).show();

                }
            });
            mRequestQueue.add(request);

        }else{
            Toast.makeText(wind_speed_cal.this, "Incomplete data!", Toast.LENGTH_SHORT).show();
        }
    }

}
