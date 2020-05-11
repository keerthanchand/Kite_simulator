package com.ski_apps.kitesimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button calculate_frame_length;
    EditText wind_speed, sh, bh, ch, support_structure;
    LinearLayout windspeed_layout;
    RadioButton two_radio, three_radio;
    TextView diagonal_length_text, total_frame_length, surface_area;
    static float sh_f, bh_f, ch_f, total_length, length, diagonal_length, support_structure_nu, surface_area_f;
    static String sh_s, bh_s, ch_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wind_speed = findViewById(R.id.edittext_wind_speed);
        sh = findViewById(R.id.edittext_sh);
        bh = findViewById(R.id.edittext_b);
        ch = findViewById(R.id.edittext_ch);
        two_radio = findViewById(R.id.radio_two_structure);
        three_radio = findViewById(R.id.radio_three_structure);
        surface_area = findViewById(R.id.textview_surface_area);
        total_frame_length = findViewById(R.id.textview_total_frame_length);
        diagonal_length_text = findViewById(R.id.textview_diagnoal_length);
        calculate_frame_length = findViewById(R.id.btn_calculate_frame_length);
        windspeed_layout = findViewById(R.id.layout_windspeed);


        Intent intent = getIntent();
        String intent_windspeed = intent.getStringExtra("wind_speed");
        wind_speed.setText(intent_windspeed);


        windspeed_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), wind_speed_cal.class);
                startActivity(intent);
            }
        });
        calculate_frame_length.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(sh.getText().toString()) && !TextUtils.isEmpty(ch.getText().toString()) && !TextUtils.isEmpty(bh.getText().toString())) {
                    sh_f = Float.parseFloat(sh.getText().toString());
                    bh_f = Float.parseFloat(bh.getText().toString());
                    ch_f = Float.parseFloat(ch.getText().toString());
                    length = 2 * sh_f + ch_f;
                    diagonal_length = (float) Math.sqrt(bh_f);
                    diagonal_length_text.setText(String.valueOf(diagonal_length));
                    if(two_radio.isChecked()){
                        support_structure_nu = 4;
                    }else{
                        support_structure_nu = 6;
                    }
                    total_length = length * 4 + diagonal_length * support_structure_nu;
                    total_frame_length.setText(String.valueOf(total_length));

                    surface_area_f = sh_f * bh_f * 4;
                    surface_area.setText(String.valueOf(surface_area_f));


                } else {
                    Toast.makeText(MainActivity.this, "Fill all the Parameters", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void onAlignmentSelected(View view) {
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_two_structure:
                support_structure_nu = 4;
                break;
            case R.id.radio_three_structure:
                support_structure_nu = 6;

                break;
        }
    }
}
