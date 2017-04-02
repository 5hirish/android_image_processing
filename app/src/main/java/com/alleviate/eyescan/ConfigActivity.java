package com.alleviate.eyescan;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConfigActivity extends AppCompatActivity {

    String state, dist, tal;
    Spinner sp_state, sp_dist, sp_tal;
    ArrayAdapter<CharSequence> adapter_states, adapter_dist, adapter_tal;
    int state_pos = 0, dist_pos = 0, tal_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        final EditText et_sup = (EditText)findViewById(R.id.et_sup_name);
        final EditText et_vill = (EditText)findViewById(R.id.et_vill);
        final EditText et_proj = (EditText)findViewById(R.id.et_project);
        sp_state = (Spinner)findViewById(R.id.sp_state);
        sp_dist = (Spinner)findViewById(R.id.sp_dist);
        sp_tal = (Spinner)findViewById(R.id.sp_tal);

        TextView tv_date = (TextView) findViewById(R.id.tv_date);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, EEE");
        tv_date.setText(sdf.format(cal.getTime()));

        adapter_states = ArrayAdapter.createFromResource(this, R.array.in_states, android.R.layout.simple_spinner_item);
        adapter_states.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_state.setAdapter(adapter_states);

        adapter_dist = ArrayAdapter.createFromResource(this, R.array.mh_dist, android.R.layout.simple_spinner_item);
        adapter_dist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_dist.setAdapter(adapter_dist);

        adapter_tal = ArrayAdapter.createFromResource(this, R.array.tal_ahmed, android.R.layout.simple_spinner_item);
        adapter_states.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();

                state_pos = position;
                state = sp_state.getItemAtPosition(position).toString();

                if (position == 5){
                    sp_dist.setAdapter(adapter_dist);
                    sp_dist.setSelection(dist_pos);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                state_pos = 0;
                state = sp_state.getItemAtPosition(0).toString();

                sp_dist.setAdapter(adapter_tal);
                sp_dist.setSelection(0);

            }
        });

        sp_dist.setAdapter(adapter_dist);
        sp_dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                dist_pos = position;
                dist = sp_dist.getItemAtPosition(position).toString();

                if (position == 0){
                    sp_tal.setAdapter(adapter_tal);
                    sp_tal.setSelection(tal_pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dist_pos = 0;
                dist = sp_dist.getItemAtPosition(0).toString();
                sp_tal.setAdapter(adapter_tal);
                sp_tal.setSelection(0);
            }
        });


        sp_tal.setAdapter(adapter_tal);
        sp_tal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tal_pos = position;
                tal = sp_tal.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tal = sp_tal.getItemAtPosition(0).toString();
                tal_pos = 0;
            }
        });

        Button bt_save = (Button)findViewById(R.id.button_save);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences spf = getSharedPreferences("Config", Context.MODE_PRIVATE);
                SharedPreferences.Editor spf_edit = spf.edit();

                spf_edit.putString("Sup_Name", et_sup.getText().toString());
                spf_edit.putString("Vill_Name", et_vill.getText().toString());
                spf_edit.putString("Proj_Name", et_proj.getText().toString());
                spf_edit.putString("State", state);
                spf_edit.putString("Dist", dist);
                spf_edit.putString("Tal", tal);

                spf_edit.commit();

                Toast.makeText(getApplicationContext(), "Details Saved", Toast.LENGTH_SHORT).show();

                finish();

            }
        });

    }
}
