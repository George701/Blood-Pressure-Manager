package com.example.chern.bloodpressurelog;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {



    Integer counter = 0;
    //Simple Data Format
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.heart_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);

        //Defying a file with path
        final String file_name = "dataSheet.txt";
        final String file_path = getFilesDir().toString() + File.separator + file_name;

        //Initialising buttons
        Button btn_save     = findViewById(R.id.btn_save);
        Button btn_show     = findViewById(R.id.btn_dispaly);
        Button btn_average  = findViewById(R.id.btn_average);

        //Initialising fields
        final EditText fld_diastolic = findViewById(R.id.diastolic_field);
        final EditText fld_systolic = findViewById(R.id.systolic_field);
        final EditText fld_hbr = findViewById(R.id.hb_field);

        //Initialising hits
        final TextView textView = findViewById(R.id.textView);

        //Checking if file exists and activation of "Average" button
        File file_data = new File(file_path);
        if (file_data.exists())
        {
            if(file_data.canRead())
            {
                try
                {
                    Log.e("(main) Test", "File can be modified <" + file_path + ">");
                    FileInputStream _FIS = new FileInputStream(new File(file_data.getAbsolutePath()));

                    InputStreamReader _ISR = new InputStreamReader(_FIS);
                    BufferedReader _BR = new BufferedReader(_ISR);

                    String data_row;

                    while( (data_row = _BR.readLine()) != null )
                    {
                        counter++;
                    }
                    _FIS.close();
                    if (counter >= 5)
                    {
                        btn_average.setEnabled(true);
                    }
                    else
                    {
                        btn_average.setEnabled(false);
                    }
                }
                catch (Exception e)
                {
                    Log.w("(main) throwback", e.getMessage());
                }
            }
            else
            {
                Log.e("(main) Test", "File cannot be modified <" + file_path + ">");
                btn_average.setEnabled(false);
            }
        }
        else
        {
            counter=0;
            btn_average.setEnabled(false);
        }

        //Initialising a functionality of "SAVE" button
        btn_save.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                textView.setText("");
                List list_text = new LinkedList();
                list_text.add(fld_diastolic);
                list_text.add(fld_systolic);
                list_text.add(fld_hbr);
                for(Object field : list_text){
                    if (((EditText) field).getText().toString().isEmpty()) {
                        Log.e("<<Form_reading>>", "Field cannot be empty!");
                        textView.setText(getText(R.string.warning_empty_field));
                        textView.setTextColor(getColor(R.color.colorHintWarning));
                        ((EditText) field).setHintTextColor(getColor(R.color.colorHintWarning));
                        ((EditText) field).requestFocus();
                        return;
                    }
                }


                //Diastolic pressure number
                EditText diastolic_data = findViewById(R.id.diastolic_field);

                //Systolic pressure number
                EditText systolic_data = findViewById(R.id.systolic_field);

                //HBpM number
                EditText hb_data = findViewById(R.id.hb_field);

                //Date and Time
                Date date = new Date();
                sdf.format(date);

                //Creating a row
                String data = diastolic_data.getText().toString()+";"+systolic_data.getText().toString()+";"+hb_data.getText().toString()+";"+date.toString();

                File file = new File(file_path);
                Button btn_average  = findViewById(R.id.btn_average);

                try
                {
                    // if the named file does not exist and was successfully created
                    if(file.createNewFile()) Log.e("(Save) Test", "File does not exist and was successfully created <" + file_path + ">");
                        // if the named file already exists
                    else Log.e("(Save) Test", "File already exists <" + file_path + ">");

                    // if the named file can be modified
                    if(file.canWrite())
                    {
                        FileOutputStream _FOS = new FileOutputStream(new File(file.getAbsolutePath()), true);
                        OutputStreamWriter _OSW = new OutputStreamWriter(_FOS);
                        _OSW.append(data).append(System.getProperty("line.separator"));
                        _OSW.close();
                        counter++;
                    }
                    else
                    {
                        Log.e("(Save) Test", "File cannot be modified <" + file_path + ">");
                    }
                }
                catch (Exception e)
                {
                    Log.w("(Save) throwback", e.getMessage());
                }
                if(!btn_average.isEnabled())
                {
                    if (counter >= 5)
                    {
                        btn_average.setEnabled(true);
                    }
                }
            }
        });

        //Initialising functionality of "SHOW" button
        btn_show.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                textView.setText("");
                textView.setTextColor(getColor(R.color.colorDefaultFont));
                File file = new File(file_path);

                try
                {
                    if(file.exists())
                    {
                        Log.e("(Show) Test", "File exists <" + file_path + ">");
                        if(file.canRead())
                        {
                            Log.e("(Show) Test", "File can be modified <" + file_path + ">");
                            FileInputStream _FIS = new FileInputStream(new File(file.getAbsolutePath()));

                            InputStreamReader _ISR = new InputStreamReader(_FIS);
                            BufferedReader _BR = new BufferedReader(_ISR);

                            String data_row;

                            StringBuilder data = new StringBuilder();
                            while( (data_row = _BR.readLine()) != null )
                            {
                                String[] data_array = data_row.split(";");
                                data.append("Date: ")
                                        .append(data_array[3])
                                        .append(System.getProperty("line.separator"))
                                        .append("D: ")
                                        .append(data_array[0])
                                        .append(", S: ")
                                        .append(data_array[1])
                                        .append(", HBR: ")
                                        .append(data_array[2])
                                        .append(System.getProperty("line.separator"))
                                        .append(System.getProperty("line.separator"));
                            }

                            textView.setText(data);
                            _FIS.close();
                        }
                        else
                        {
                            Log.e("(Show) Test", "File cannot be modified <" + file_path + ">");
                        }
                    }
                    else
                    {
                        Log.e("(Show) Test", "File does not exist <" + file_path + ">");
                    }
                }
                catch(Exception e)
                {
                    Log.w("(Show) throwback", e.getMessage());
                }
            }
        });


        //Initialising functionality of "AVERAGE" button
        btn_average.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                textView.setText("");
                textView.setTextColor(getColor(R.color.colorDefaultFont));
                File file = new File(file_path);

                try
                {
                    if(file.exists())
                    {
                        Log.e("(avg) Test", "File exists <" + file_path + ">");
                        if(file.canRead())
                        {
                            Log.e("(avg) Test", "File can be modified <" + file_path + ">");
                            FileInputStream _FIS = new FileInputStream(new File(file.getAbsolutePath()));

                            InputStreamReader _ISR = new InputStreamReader(_FIS);
                            BufferedReader _BR = new BufferedReader(_ISR);

                            String data_row;

                            double avr_diastolic=0;
                            double avr_systolic=0;
                            double avr_hbr=0;

                            StringBuilder data = new StringBuilder();

                            while( (data_row = _BR.readLine()) != null )
                            {
                                String[] data_array = data_row.split(";");
                                avr_diastolic += Double.parseDouble(data_array[0]);
                                avr_systolic  += Double.parseDouble(data_array[1]);
                                avr_hbr       += Double.parseDouble(data_array[2]);
                            }
                            avr_diastolic   = avr_diastolic/counter;
                            avr_systolic    = avr_systolic/counter;
                            avr_hbr         = avr_hbr/counter;

                            data.append("Average Diastolic Pressure: ")
                                    .append(avr_diastolic)
                                    .append(System.getProperty("line.separator"))
                                    .append("Average Systolic Pressure: ")
                                    .append(avr_systolic)
                                    .append(System.getProperty("line.separator"))
                                    .append("Average Heart Beat Rate: ")
                                    .append(avr_hbr)
                                    .append(System.getProperty("line.separator"));

                            textView.setText(data);
                            _FIS.close();
                        }
                        else
                        {
                            Log.e("(avg) Test", "File cannot be modified <" + file_path + ">");
                        }
                    }
                    else
                    {
                        Log.e("(avr) Test", "File does not exist <" + file_path + ">");
                    }
                }
                catch(Exception e)
                {
                    Log.w("(avr) throwback", e.getMessage());
                }

//                view.setEnabled(false);
            }
        });

        fld_diastolic.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                textView.setText("");
                fld_diastolic.setHintTextColor(getColor(R.color.colorDefaultFont));
                fld_diastolic.setTextColor(getColor(R.color.colorDefaultFont));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fld_systolic.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                textView.setText("");
                fld_systolic.setHintTextColor(getColor(R.color.colorDefaultFont));
                fld_systolic.setTextColor(getColor(R.color.colorDefaultFont));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
       fld_hbr.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                textView.setText("");
                fld_hbr.setHintTextColor(getColor(R.color.colorDefaultFont));
                fld_hbr.setTextColor(getColor(R.color.colorDefaultFont));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*File file = new File(file_path);
                if(file.exists()){
                    file.delete();
                }*/

    }
}
